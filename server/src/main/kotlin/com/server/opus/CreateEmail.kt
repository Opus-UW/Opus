package com.server.opus

import java.util.*
import javax.mail.MessagingException
import javax.mail.Session
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage


/* Class to demonstrate the use of Gmail Create Email API  */
object CreateEmail {
    /**
     * Create a MimeMessage using the parameters provided.
     *
     * @param toEmailAddress   email address of the receiver
     * @param fromEmailAddress email address of the sender, the mailbox account
     * @param subject          subject of the email
     * @param bodyText         body text of the email
     * @return the MimeMessage to be used to send email
     * @throws MessagingException - if a wrongly formatted address is encountered.
     */
    @Throws(MessagingException::class)
    fun createEmail(
        toEmailAddress: String?,
        fromEmailAddress: String?,
        subject: String?,
        bodyText: String?
    ): MimeMessage {
        val props = Properties()
        val session: Session = Session.getDefaultInstance(props, null)
        val email = MimeMessage(session)
        email.setFrom(InternetAddress(fromEmailAddress))
        email.addRecipient(
            javax.mail.Message.RecipientType.TO,
            InternetAddress(toEmailAddress)
        )
        email.setSubject(subject)
        email.setText(bodyText)
        return email
    }
}