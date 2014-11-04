package utils;

import java.io.File;
import java.io.StringReader;
import java.util.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import main.Logger;

import org.w3c.dom.*;
import org.xml.sax.InputSource;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;
import data.*;


/**
 * Dieser Converter besitzt zwei statische Methoden, mit denen TodoProject-Objekte in XML
 * und umgekehrt konvertiert werden können
 * @author Tobias Schaber
 *
 */
public final class TodoProjectToXmlConverter {
	
	public static String escapeXmlString(String in) {
//		in = in.replace("\"", "&qout;");
		in = in.replace("&", "&amp;");
//		in = in.replace("'", "&apos;");
		in = in.replace("<", "&lt;");
		in = in.replace(">", "&gt;");
		in = in.replace("\"", "&quot;");
		
		
		return in;
		
	}
	
	
	
	/**
	 * konvertiert einen XML-String in ein TodoProject-Objekt
	 * @param xmlString
	 * @return
	 */
	public static final TodoProject convertXMLToTodoProject(String xmlString) throws Exception {

		
		Logger.getInstance().log("Starte Konvertierung von XML nach TP", Logger.LOGLEVEL_INFO);
		
		final TodoProject tp = new TodoProject();
		
		final DocumentBuilderFactory dbf 	= DocumentBuilderFactory.newInstance();
		final DocumentBuilder db			= dbf.newDocumentBuilder();
		final Document doc				= db.parse(new InputSource(new StringReader(xmlString)));
		doc.getDocumentElement().normalize();
		
		final NodeList todoProjectLists = doc.getElementsByTagName("todoList");
		
				
		for(int i1=0; i1<todoProjectLists.getLength(); i1++) {
			
			final Node nd = todoProjectLists.item(i1);
			
			if(nd.getNodeType() == Node.ELEMENT_NODE) {
				
				final Element el = (Element) nd;
				
				final String name			= el.getAttribute("name");
				final int lastUsedId		= Integer.parseInt(el.getAttribute("lastUsedId"));
				
				final TodoList tl = new TodoList(name, tp);
				tl.lastUsedId = lastUsedId;
				
				if(el.getAttribute("locked").equals("true")) 	tl.setLockedStatus(true);
				else											tl.setLockedStatus(false);
				
								
				// -----------------------------------------------------------
				
				final NodeList todoItemStackList = el.getChildNodes();
				
				for(int i2=0; i2<todoItemStackList.getLength(); i2++) {
					
					final Node nd2 = todoItemStackList.item(i2);
					
					
					if(nd2.getNodeType() == Node.ELEMENT_NODE) {
						
					
						// TodoItemStack-Element
						final Element el2 = (Element) nd2;
						
						
						final int priority = Integer.parseInt(el2.getAttribute("priority"));
						final int todoId = Integer.parseInt(el2.getAttribute("todoId"));
						final String tisName = el2.getAttribute("name");
						final String category = el2.getAttribute("category");
						
						Date dSt = null;
						try {	dSt = (new Date(Long.parseLong(el2.getAttribute("planStart")))); }
						catch(Exception e){ }
						
						Date dEn = null;
						try {	dEn = (new Date(Long.parseLong(el2.getAttribute("planEnd")))); }
						catch(Exception e){ }
						
						
						Date d = null;
						try {	d = (new Date(Long.parseLong(el2.getAttribute("reminderDate")))); }
						catch(Exception e){ }
						
						
						final String reminderIsActive = el2.getAttribute("reminderIsActive");
						
						boolean reminderIsAct = false;
						if(reminderIsActive.equals("true")) reminderIsAct = true;
						
						final TodoItemStack tis = new TodoItemStack(tisName, todoId, tl, true, priority, dSt, dEn, category);
						tis.setReminderActivationStatus(reminderIsAct);
						tis.setReminderDate(d);	
						
						// ------------------------------------------------------------------------------------------------
						
						final NodeList todoItemList = el2.getChildNodes();
						
						for(int i3=0; i3<todoItemList.getLength(); i3++) {
							
							
							if(todoItemList.item(i3).getNodeName().equals("attachments")) {
								
								final Node attN1 = todoItemList.item(i3);
								
								
								if(attN1.getNodeType() == Node.ELEMENT_NODE) {
									final Element attEl1 = (Element) attN1;
									
									final NodeList attList = attEl1.getChildNodes();
									
									for(int ati4=0; ati4<attList.getLength(); ati4++) {
										
										final Node nx = attList.item(ati4);
										
										if(nx.getNodeType() == Node.ELEMENT_NODE) {
										
											final Element elx = (Element) nx;
											
											final int storageMode = Integer.parseInt(elx.getAttribute("storageMode"));
											final String pathToFile = elx.getAttribute("pathToFile");
											
											Attachment att;
											
											if(storageMode == Attachment.STORE_MODE_INTERN) {
												
												final String contentString = elx.getTextContent();
												BASE64Decoder b64decoder = new BASE64Decoder();
												byte[] content = b64decoder.decodeBuffer(contentString);
												
												att = new Attachment(tis, storageMode, new File(pathToFile), content);
												
											} else {
												
												att = new Attachment(storageMode, new File(pathToFile));
											}
											
											tis.addAttachment(att);
										}
									}
								}

							} else {
								
								final Node nd3 = todoItemList.item(i3);
								
								if(nd3.getNodeType() == Node.ELEMENT_NODE) {
										
								
									final Element el3 = (Element) nd3;
									
									final String status = el3.getAttribute("status");
	
									final String desc = el3.getElementsByTagName("description").item(0).getTextContent();
									final String text = el3.getElementsByTagName("text").item(0).getTextContent();
	
									
									
									final Date dmod = new Date(Long.parseLong(el3.getAttribute("modificationDate"))); 
							
									final TodoItem ti = new TodoItem(desc, status, text, dmod , tis);
									
							
									tis.getTodoHistory().add(ti);
									
								}
							}
						}
						tl.getTodoList().add(tis);
					}
				}
				tp.addList(tl);
		
			}
		}
		

		return tp;
		
	}
	

	
	/**
	 * konvertiert ein TodoProject-Objekt in einen XML-String
	 * @param obj
	 * @return
	 */
	public static final String convertTodoProjectToXML(TodoProject obj) {
		
		Logger.getInstance().log("Starte Konvertierung von TP nach XML", Logger.LOGLEVEL_INFO);
		
		
		String xmlString = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";
		xmlString += "<todoProject>\n";
		
		
		
		for(TodoList tl : obj.getLists()) {
			
			xmlString += "<todoList name=\"" + escapeXmlString(tl.getListName()) + "\" lastUsedId=\"" + tl.lastUsedId + "\" locked=\"" + tl.isLocked() + "\">\n";
			

				for(TodoItemStack tis : tl.getTodoList()) {
					

					
					String reminderDate = "";
					String planStartDate = "";
					String planEndDate = "";
					String category = "";
					if(tis.getReminderDate() != null)	reminderDate 	= ""+tis.getReminderDate().getTime();
					if(tis.getPlanStart() != null)		planStartDate 	= ""+tis.getPlanStart().getTime();
					if(tis.getPlanEnd() != null)		planEndDate 	= ""+tis.getPlanEnd().getTime();
					if(tis.getCategory() != null)		category		= tis.getCategory();
					
					xmlString += "<todoItemStack name=\"" + escapeXmlString(tis.getName()) + "\" todoId=\"" + tis.getTodoId() + "\" reminderDate=\"" + reminderDate + "\" reminderIsActive=\"" + tis.getReminderActivationStatus() + "\" priority=\"" + tis.getPriority() + "\" planStart=\"" + planStartDate + "\" planEnd=\"" + planEndDate + "\" category=\"" + category + "\">\n";
					
					
					xmlString += "<attachments>\n";
					try {
					for(Attachment att : tis.getAttachments()) {
						
						String pathToFile = "";
						if(att.getPathToFile() != null) { pathToFile = att.getPathToFile().getAbsolutePath(); }
							
						xmlString += "<attachment storageMode=\"" + att.getStorageMode() + "\" pathToFile=\"" + pathToFile + "\">";
						
						if(att.getStorageMode() == Attachment.STORE_MODE_INTERN) {
							
							BASE64Encoder b64enc = new BASE64Encoder();
							String b64content = b64enc.encode(att.getContent());
							
							xmlString += b64content;
							 
						}
						
						xmlString += "</attachment>\n";
						
					}
					
					} catch(Exception e){ e.printStackTrace(); }
					
					xmlString += "</attachments>\n";
					
					if(tis.getTodoHistory() != null) {
						for(TodoItem ti : tis.getTodoHistory()) {
						
							String modDate = "";
							if(ti.getModificationDate() != null)	modDate = ""+ti.getModificationDate().getTime();
					
													
							xmlString += "<todoItem status=\"" + escapeXmlString(ti.getStatus()) + "\" modificationDate=\"" + escapeXmlString(modDate) + "\">\n";
							
							xmlString += "<description>" + escapeXmlString(ti.getDescription()) + "</description>\n";
							
							xmlString += "<text>" + escapeXmlString(ti.getText()) + "</text>\n";
							
							xmlString += "</todoItem>\n";
							
						}
					}
					xmlString += "</todoItemStack>\n";
					
				}
								
			xmlString += "</todoList>\n";
		}
		
		
		xmlString += "</todoProject>\n";
		
		return xmlString;
		
	}

}
