package com.kustomer.chatreactnative

import android.os.Bundle
import com.facebook.react.bridge.Arguments
import com.facebook.react.bridge.ReadableMap
import com.facebook.react.bridge.WritableMap
import com.google.firebase.messaging.RemoteMessage
import com.kustomer.core.models.KusChatSetting
import com.kustomer.core.models.KusIdentifiedCustomer
import com.kustomer.core.models.chat.*
import com.kustomer.core.models.kb.KusKbArticle
import com.kustomer.core.models.pubnub.KusPresenceEvent

fun remoteMessageFromReadableMap(readableMap: ReadableMap): RemoteMessage {
  val bundle = Bundle();

  if (readableMap.hasKey("collapseKey")) {
    bundle.putString("collapse_key", readableMap.getString("collapseKey"))
  }

  if (readableMap.hasKey("from")) {
    bundle.putString("from", readableMap.getString("from"))
  }

  if (readableMap.hasKey("to")) {
    bundle.putString("google.to", readableMap.getString("to"))
  }

  if (readableMap.hasKey("messageId")) {
    bundle.putString("google.message_id", readableMap.getString("messageId"))
  }

  if (readableMap.hasKey("messageType")) {
    bundle.putString("message_type", readableMap.getString("messageType"))
  }

  if (readableMap.hasKey("ttl")) {
    bundle.putInt("google.ttl", readableMap.getInt("ttl"))
  }

  if (readableMap.hasKey("data")) {
    val messageData = readableMap.getMap("data")
    val iterator = messageData!!.keySetIterator()
    while (iterator.hasNextKey()) {
      val key = iterator.nextKey()
      bundle.putString(key, messageData.getString(key))
    }
  }

  return RemoteMessage(bundle)
}

fun toWritableKustomerConversation(conversation: KusConversation): WritableMap =
  Arguments.makeNativeMap(
    mapOf(
      "id" to conversation.id,
      "createdAt" to conversation.createdAt,
      "lastMessageAt" to conversation.lastMessageAt,
//      "status" to conversation.?? //todo
      "lockedAt" to conversation.lockedAt,
      "lockedByCustomer" to conversation.lockedByCustomer,
      "lockReason" to conversation.lockReason,
//      "preview" to toWritableKustomerPreview(conversation.preview),//todo
      "respondingUserIds" to Arguments.fromList(conversation.responders),
      "unreadMessageCount" to conversation.unreadMessageCount,
//      "customers" to conversation.??
      // convert with toWritableKustomerUser, use toList, and then Arguments.fromList, only if not null, otherwise null
      "users" to conversation.users?.let { Arguments.fromList(it.toList().map { u -> toWritableKustomerUser(u) }) },
    )
  )

fun toWritableKustomerChatMessage(message: KusChatMessage): WritableMap = Arguments.makeNativeMap(
  mapOf(
    "id" to message.id,
    "conversationId" to message.conversationId,
    "createdAt" to message.createdAt,
    "direction" to message.direction.name,
    "directionType" to message.directionType,
    "body" to message.body,
    "attachments" to message.attachments?.let { Arguments.fromList(it.map { a -> toWritableKustomerChatAttachment(a) }) },
    "sentByUser" to toWritableKustomerUser(message.sentByUser),
    "sentById" to message.sentById,
    "pubNubTimetoken" to message.pubnubTimetoken,
    "templateType" to message.template?.templateType
  )
)

fun toWritableKustomerChatAttachment(attachment: KusChatAttachment): WritableMap =
  Arguments.makeNativeMap(
    mapOf(
      "id" to attachment.id,
      "name" to attachment.name,
      "contentType" to attachment.contentType,
      "contentLength" to attachment.contentLength,
      "url" to attachment.link,
      "createdAt" to attachment.createdAt,
      "updatedAt" to attachment.updatedAt,
    )
  )

fun toWritableKustomerChatSetting(setting: KusChatSetting): WritableMap =
  Arguments.makeNativeMap(
    mapOf(
      "id" to setting.id,
//    "widgetType" to setting.widgetType, // todo KusPreferredView
      "teamName" to setting.teamName,
      "teamIconUrl" to setting.teamIconUrl,
      "greeting" to setting.greeting,
      "activeFormId" to setting.activeFormId,
      "activeAssistant" to setting.activeAssistant,
      "enabled" to setting.enabled,
      "kbId" to setting.kbId,
      "brandId" to setting.brandId,
//      "availability" to setting.availability, // todo KusChatAvailability
      "offHoursImageUrl" to setting.offHoursImageUrl,
      "offHoursMessage" to setting.offHoursMessage,
//      "volumeControl" to setting.volumeControl, // todo KusVolumeControlSetting
      "defaultWaitMessage" to setting.defaultWaitMessage,
      "closableChat" to setting.closableChat,
      "singleSessionChat" to setting.singleSessionChat,
      "noHistory" to setting.noHistory,
      "showBrandingIdentifier" to setting.showBrandingIdentifier,
      "showTypingIndicatorCustomerWeb" to setting.showTypingIndicatorCustomerWeb,
      "showTypingIndicatorWeb" to setting.showTypingIndicatorWeb,
//      "sharedPubNubKeySet" to setting.sharedPubNubKeySet, // todo PubNubKeySet
//      "orgPubNubKeysets" to setting.orgPubNubKeysets, // todo List<PubNubKeySet>?
//      "assistantRules" to setting.assistantRules, //  todo List<KusAssistantRule>?
      "disableAttachments" to setting.disableAttachments,
      "outboundChatEnabled" to setting.outboundChatEnabled,
//      "sentry" to setting.sentry, //  todo KusSentryConfig?
    )
  )

fun toWritableKustomerUser(user: KusUser?): WritableMap? = user?.let {
  Arguments.makeNativeMap(
    mapOf(
      "id" to it.id,
      "avatarUrl" to it.avatarUrl,
      "displayName" to it.displayName,
    )
  )
}

fun toWritableKustomerSatisfaction(satisfaction: KusSatisfaction): WritableMap {
  return Arguments.makeNativeMap(
    mapOf(
      "id" to satisfaction.id,
//      "rawJson" to satisfaction.rawJson, // TODO rawJson: @RawValue Any?
      "timetoken" to satisfaction.timetoken,
//      "response" to satisfaction.response, // TODO KusCsatResponse?
//      "form" to satisfaction.form, // TODO KusCsatForm?
      "conversationId" to satisfaction.conversationId,
      "lockedAt" to satisfaction.lockedAt
    )
  )
}

fun toWritableKustomerAssistant(assistant: KusAssistant?): WritableMap {
  return Arguments.makeNativeMap(
    mapOf(
      "id" to assistant?.id,
      "rawJson" to assistant?.rawJson,
      "name" to assistant?.name,
      "publicName" to assistant?.publicName,
      "avatarUrl" to assistant?.avatarUrl,
      "dialog" to assistant?.dialog,
      "node" to assistant?.node,
      "messages" to Arguments.fromList(assistant?.messages?.toTypedArray()
        ?.map { toWritableKustomerChatMessage(it) })
    )
  )
}

fun toWritableKustomerPreview(preview: KusConversationPreview?): WritableMap {
  return Arguments.makeNativeMap(
    mapOf(
      "preview" to preview?.toString()
    )
  )
}

fun toWritableKustomerTrackingIdentity(identity: KusIdentifiedCustomer): WritableMap {
  return Arguments.makeNativeMap(
    mapOf(
      "id" to identity.id,
      "customerId" to identity.customerId,
      "trackingId" to identity.trackingId,
      "trackingToken" to identity.token,
      "email" to identity.email,
      "externalId" to identity.externalId,
      "verified" to identity.verified,
      "createdAt" to identity.createdAt,
      "updatedAt" to identity.updatedAt,
      "rawJson" to identity.rawJson,
    )
  )
}

fun toWritableKustomerKbArticle(article: KusKbArticle): WritableMap {
  return Arguments.makeNativeMap(
    mapOf(
      "id" to article.id,
      "articleId" to article.articleId,
      "title" to article.title,
      "metaDescription" to article.metaDescription?.toString(),
      "htmlBody" to article.htmlBody,
      "language" to article.lang,
      "published" to article.published,
      "scope" to article.scope.name,
      "updatedAt" to article.updatedAt,
      "version" to article.version,
      "kbUrl" to article.kbUrl,
      "kbId" to article.knowledgeBaseId,
      "slug" to article.slug,
      "hash" to article.hash,
      "rawJson" to article.rawJson,
    )
  )
}

fun toKusSatisfactionNetworkPostBody(map: ReadableMap): KusSatisfactionNetworkPostBody {
  return KusSatisfactionNetworkPostBody(
    answers = listOf(), // TODO
    rating = map.getInt("rating"),
    submittedAt = map.getDouble("submittedAt").toLong(),
  )
}

fun toKusMessageAction(map: ReadableMap): KusMessageAction {
  return KusMessageAction(
    displayText = map.getString("displayText") as String,
    value = map.getString("value") as String
  )
}

fun toKusTypingStatus(status: String): KusTypingStatus {
  return when (status) {
    "USER_TYPING_STARTED" -> KusTypingStatus.USER_TYPING_STARTED
    "USER_TYPING_ENDED" -> KusTypingStatus.USER_TYPING_ENDED
    "CUST_TYPING_STARTED" -> KusTypingStatus.CUST_TYPING_STARTED
    "CUST_TYPING_ENDED" -> KusTypingStatus.CUST_TYPING_ENDED
    else -> {
      KusTypingStatus.TYPING_UNKNOWN
    }
  }
}

fun toKusPresenceEvent(presence: String): KusPresenceEvent {
  return when (presence) {
    "ONLINE" -> KusPresenceEvent.ONLINE
    "OFFLINE" -> KusPresenceEvent.OFFLINE
    else -> {
      KusPresenceEvent.OFFLINE
    }
  }
}


fun toKusEmail(email: String): KusEmail {
  return KusEmail(email = email)
}

fun toKusPhone(phone: String): KusPhone {
  return KusPhone(phone = phone)
}

fun toKusCustomerDescribeAttributes(attributes: ReadableMap): KusCustomerDescribeAttributes{
  val emails = ArrayUtil.toArray(attributes.getArray("email")).asList().map { toKusEmail(it as String) }
  val phones = ArrayUtil.toArray(attributes.getArray("phones")).asList().map { toKusPhone(it as String) }

  return KusCustomerDescribeAttributes(
    emails = emails,
    phones = phones
  )
}

