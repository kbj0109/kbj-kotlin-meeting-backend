package com.kbj.meeting.service

import com.kbj.meeting.constant.BadRequestException
import com.kbj.meeting.constant.NotFoundException
import com.kbj.meeting.controller.MessageSendRequest
import com.kbj.meeting.repository.MatchingRepository
import com.kbj.meeting.repository.MessageRepository
import com.kbj.meeting.repository.UserRepository
import com.kbj.meeting.repository.entity.Matching
import com.kbj.meeting.repository.entity.Message
import com.kbj.meeting.repository.entity.MessageLevelEnum
import com.kbj.meeting.repository.entity.MessageStatusEnum
import org.springframework.stereotype.Service

@Service
class MessageService(
    private val userRepository: UserRepository,
    private val messageRepository: MessageRepository,
    private val matchingRepository: MatchingRepository,
) {
    fun sendMessageAfterCheck(
        fromUserId: Long,
        data: MessageSendRequest,
    ): Message {
        this.userRepository.findById(fromUserId).orElseThrow { NotFoundException() }

        val isPossible =
            this.checkIfPossibleToSendMessage(
                data.toUserId,
                fromUserId,
                data.messageLevel as MessageLevelEnum,
            )

        if (!isPossible) {
            throw BadRequestException()
        }

        this.messageRepository.updatePreviousMessage(
            data.toUserId,
            fromUserId,
            MessageStatusEnum.Deactivated,
        )

        val item =
            this.messageRepository.save(
                Message(
                    fromUserId = fromUserId,
                    toUserId = data.toUserId,
                    messageLevel = data.messageLevel as MessageLevelEnum,
                    messageStatus = MessageStatusEnum.Activated,
                    text = data.text,
                    reason = null,
                ),
            )

        return item
    }

    fun checkIfPossibleToSendMessage(
        toUserId: Long,
        fromUserId: Long,
        messageLevel: MessageLevelEnum,
    ): Boolean {
        // 이미 매칭된 경우에는 메세지를 보낼 수 없음
        val item1 = this.matchingRepository.readOneByUserIdAndMatchingUserId(toUserId, fromUserId)
        val item2 = this.matchingRepository.readOneByUserIdAndMatchingUserId(fromUserId, toUserId)

        if (item1 != null || item2 != null) {
            return false
        }

        // 혹시 이전에 보낸 메세지가 있다면 더 높은 레벨의 메세지만 가능
        val item =
            this.messageRepository.readPreviousMessage(
                fromUserId,
                toUserId,
                MessageStatusEnum.Activated,
            )

        if (item == null || item.messageLevel < messageLevel) {
            return true
        }

        return false
    }

    fun updateMessageStatus(
        messageId: Long,
        toUserId: Long,
        messageStatus: MessageStatusEnum,
        reason: String?,
    ): Message {
        val item = this.messageRepository.findById(messageId).orElseThrow { NotFoundException() }
        if (item.toUserId != toUserId || item.messageStatus != MessageStatusEnum.Activated) {
            throw BadRequestException()
        }

        this.messageRepository.updateMessageStatus(messageId, messageStatus, reason)

        if (messageStatus == MessageStatusEnum.Accepted) {
            this.matchingRepository.save(Matching(toUserId, item.fromUserId, item.id!!))
            this.matchingRepository.save(Matching(item.fromUserId, toUserId, item.id!!))
        }

        return this.messageRepository.findById(messageId).orElseThrow { NotFoundException() }
    }
}
