package b_utilities;

import java.io.File;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import a_testBase.BaseClass;

/**
 * Emails the Extent and Excel reports when {@code email=yes}.
 *
 * <p>
 * Attachment paths are resolved from the project's own reports folder rather
 * than from a hard-coded desktop path, so this works on any machine that checks
 * the project out.
 * </p>
 */
public class MonitoringMail {

	public void sendMail(String mailServer, String from, String[] to, String subject, String messageBodyText,
			String attachmentPath, String excelReportName, String extentReportName, String suiteName)
			throws MessagingException, AddressException {

		Properties props = new Properties();
		props.put("mail.smtp.host", mailServer);
		props.put("mail.smtp.port", "465");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.starttls.required", "true");
		props.put("mail.smtp.ssl.enable", "true");
		props.put("mail.smtp.ssl.protocols", "TLSv1.2");
		props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");

		Authenticator auth = new SMTPAuthenticator();
		Session session = Session.getDefaultInstance(props, auth);

		Transport bus = session.getTransport("smtp");
		bus.connect();

		Message message = new MimeMessage(session);
		message.addHeader("X-Priority", "1");
		message.setFrom(new InternetAddress(from));

		InternetAddress[] addressTo = new InternetAddress[to.length];
		for (int i = 0; i < to.length; i++) {
			addressTo[i] = new InternetAddress(to[i]);
		}
		message.setRecipients(Message.RecipientType.TO, addressTo);
		message.setSubject(subject);

		String html = "<body><div>"
				+ "<p>Hi Team,</p>"
				+ "<p>Automation results for the <b>the application under test</b> are attached.</p>"
				+ "<p>Suite: <b>" + suiteName + "</b><br/>"
				+ "Application: <b>" + BaseClass.getAppUrl() + "</b></p>"
				+ "<p>The Extent HTML report holds the step detail and a screenshot for every failure. "
				+ "The Excel report holds the per-test-case summary against the manual test case IDs.</p>"
				+ "<p>Thanks &amp; Regards</p>"
				+ "<p><b style=\"color:black;\">Innodata QA Automation Team</b></p><hr/>"
				+ "<p><strong>Note:</strong> <em>Auto generated email - please do not reply.</em></p>"
				+ "</div></body>";

		BodyPart body = new MimeBodyPart();
		body.setContent(html, "text/html");

		MimeMultipart multipart = new MimeMultipart();
		multipart.addBodyPart(body);

		addAttachmentIfPresent(multipart, attachmentPath + File.separator + "ExcelReport.xlsx",
				excelReportName + ".xlsx");
		addAttachmentIfPresent(multipart, attachmentPath + File.separator + "ExtentReport.html",
				extentReportName + ".html");

		message.setContent(multipart);
		Transport.send(message);
		System.out.println("Report email sent to " + String.join(", ", to));
		bus.close();
	}

	/** Attaches a file only when it actually exists, so a missing report cannot abort the send. */
	private void addAttachmentIfPresent(MimeMultipart multipart, String path, String displayName)
			throws MessagingException {
		File file = new File(path);
		if (!file.exists()) {
			System.out.println("Attachment not found, skipping: " + path);
			return;
		}
		MimeBodyPart attachment = new MimeBodyPart();
		DataSource source = new FileDataSource(file);
		attachment.setDataHandler(new DataHandler(source));
		attachment.setFileName(displayName);
		multipart.addBodyPart(attachment);
	}

	private class SMTPAuthenticator extends javax.mail.Authenticator {
		@Override
		public PasswordAuthentication getPasswordAuthentication() {
			return new PasswordAuthentication(TestConfig.from, TestConfig.password);
		}
	}
}
