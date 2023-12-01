package com.server.opus

import com.google.api.services.gmail.model.Message
import org.apache.commons.codec.binary.Base64
import java.io.ByteArrayOutputStream
import java.io.IOException
import javax.mail.MessagingException
import javax.mail.internet.MimeMessage

/* Class to demonstrate the use of Gmail Create Message API */
object CreateMessage {
    /**
     * Create a message from an email.
     *
     * @param emailContent Email to be set to raw of message
     * @return a message containing a base64url encoded email
     * @throws IOException        - if service account credentials file not found.
     * @throws MessagingException - if a wrongly formatted address is encountered.
     */
    @Throws(MessagingException::class, IOException::class)
    fun createMessageWithEmail(emailContent: MimeMessage): Message {
        val buffer = ByteArrayOutputStream()
        emailContent.writeTo(buffer)
        val bytes = buffer.toByteArray()
        val encodedEmail = Base64.encodeBase64URLSafeString(bytes)
        val message = Message()
        message.setRaw(encodedEmail)
        return message
    }
}