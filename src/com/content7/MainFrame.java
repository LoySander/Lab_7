package com.content7;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.*;
import java.nio.file.Paths;
import javax.swing.*;
import javax.swing.text.*;
import javax.swing.text.html.*;
import javax.swing.ImageIcon;

@SuppressWarnings("serial")

public class MainFrame extends JFrame {
    private static final String FRAME_TITLE= "Клиент мгновенных сообщений";
    private static final int FRAME_MINIMUM_WIDTH= 500;
    private static final int FRAME_MINIMUM_HEIGHT= 500;
    private static final int FROM_FIELD_DEFAULT_COLUMNS= 10;
    private static final int TO_FIELD_DEFAULT_COLUMNS= 20;
    private static final int INCOMING_AREA_DEFAULT_ROWS= 10;
    private static final int OUTGOING_AREA_DEFAULT_ROWS= 5;
    private static final int SMALL_GAP= 5;
    private static final int MEDIUM_GAP= 10;
    private static final int LARGE_GAP= 15;
    private static final int SERVER_PORT= 4567;
    private final JTextField textFieldFrom;
    private final JTextField textFieldTo;
    private final JEditorPane textAreaIncoming;
    private final JTextArea textAreaOutgoing;
    private InstantMessenger refactoring;
    private Peer client;
    private ImageIcon icon;
    private StringBuffer incomingText;
    public MainFrame() {
        super(FRAME_TITLE);
        client = new Peer();
        icon = new ImageIcon("Smile.png");
        incomingText = new StringBuffer();
        // Текстовая область для отображения полученных сообщений;
        textAreaIncoming = new JEditorPane();
        textAreaIncoming.setContentType("text/html");
        textAreaIncoming.setEditable(false);
        setMinimumSize( new Dimension(FRAME_MINIMUM_WIDTH, FRAME_MINIMUM_HEIGHT));
        // Центрирование окна
        final Toolkit kit = Toolkit.getDefaultToolkit();
        setLocation((kit.getScreenSize().width - getWidth()) / 2, (kit.getScreenSize().height - getHeight()) / 2);
        // Контейнер, обеспечивающий прокрутку текстовой области
        final JScrollPane scrollPaneIncoming =  new JScrollPane(textAreaIncoming);
        // Подписиполей
        final JLabel labelFrom = new JLabel("Подпись");
        final JLabel labelTo = new JLabel("Получатель");
        // Поля ввода имени пользователя и адреса получателя
        textFieldFrom = new JTextField(FROM_FIELD_DEFAULT_COLUMNS);
        textFieldTo = new JTextField(TO_FIELD_DEFAULT_COLUMNS);
        // Текстовая область для ввода сообщения
        textAreaOutgoing = new JTextArea(OUTGOING_AREA_DEFAULT_ROWS, 0);
        // Контейнер, обеспечивающий прокрутку текстовой области
        final JScrollPane scrollPaneOutgoing =  new JScrollPane(textAreaOutgoing);
        // Панель ввода сообщения
        final JPanel messagePanel = new JPanel();
        messagePanel.setBorder( BorderFactory.createTitledBorder("Сообщение"));
        // Кнопка отправки сообщения
        final JButton sendButton = new JButton("Отправить");
        sendButton.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                client.SetSender(textFieldFrom.getText());
                client.SetdestinationAddress(textFieldTo.getText());
                refactoring.sendMessage(textAreaOutgoing.getText(),client);// ?
            }
        });
        // Компоновка элементов панели "Сообщение"
        final GroupLayout layout2 = new GroupLayout(messagePanel);
        messagePanel.setLayout(layout2);
        layout2.setHorizontalGroup(layout2.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout2.createParallelGroup(GroupLayout.Alignment.TRAILING)
                        .addGroup(layout2.createSequentialGroup()
                                .addComponent(labelFrom)
                                .addGap(SMALL_GAP)
                                .addComponent(textFieldFrom)
                                .addGap(LARGE_GAP)
                                .addComponent(labelTo)
                                .addGap(SMALL_GAP)
                                .addComponent(textFieldTo))
                        .addComponent(scrollPaneOutgoing)
                        .addComponent(sendButton))
                .addContainerGap());
        layout2.setVerticalGroup(layout2.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout2.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(labelFrom)
                        .addComponent(textFieldFrom)
                        .addComponent(labelTo)
                        .addComponent(textFieldTo))
                .addGap(MEDIUM_GAP)
                .addComponent(scrollPaneOutgoing)
                .addGap(MEDIUM_GAP)
                .addComponent(sendButton)
                .addContainerGap());
        // Компоновка элементов фрейма
        final GroupLayout layout1 = new GroupLayout(getContentPane());
        setLayout(layout1);
        layout1.setHorizontalGroup(layout1.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout1.createParallelGroup()
                        .addComponent(scrollPaneIncoming)
                        .addComponent(messagePanel))
                .addContainerGap());
        layout1.setVerticalGroup(layout1.createSequentialGroup()
                .addContainerGap()
                .addComponent(scrollPaneIncoming)
                .addGap(MEDIUM_GAP)
                .addComponent(messagePanel)
                .addContainerGap());
       refactoring = new InstantMessenger(this);

       
        // Создание и запуск потока-обработчика запросов
        /*new Thread(new Runnable() {
            @Override public void run() {
                try {
                    final ServerSocket serverSocket =  new ServerSocket(SERVER_PORT);
                    while(!Thread.interrupted()) {
                        final Socket socket = serverSocket.accept();
                        final DataInputStream in = new DataInputStream( socket.getInputStream());
                        // Читаем имя отправителя
                        final String senderName = in.readUTF();
                        // Читаем сообщение
                        final String message = in.readUTF();
                        // Закрываем соединение
                        socket.close();
                        // Выделяем IP-адрес
                        final String address =  ((InetSocketAddress) socket
                                .getRemoteSocketAddress())
                                .getAddress()
                                .getHostAddress();
                        // Выводим сообщение в текстовую область
                        textAreaIncoming.append(senderName +  " ("   + address + "): " +  message + "\n");
                    }
                } catch(IOException e) {
                    e.printStackTrace(); JOptionPane.showMessageDialog(MainFrame.this,
                            "Ошибка в работе сервера", "Ошибка", JOptionPane.ERROR_MESSAGE);
                }
            }
            }).start();*/
        }
    public JTextArea getTextAreaOutgoing() {
        return textAreaOutgoing;
    }
    public int getServerPort() {
        return SERVER_PORT;
    }
    public JEditorPane getTextAreaIncoming() {
        return textAreaIncoming;
    }
    public synchronized  void appendMessage(String message){
        String smile = ":)";

        String oldmessage = textAreaIncoming.getText();
        while(message.contains(smile)) {
            int pos = message.indexOf(smile);
            System.out.println(pos);
            URL imgsrc= null;
            try {
                imgsrc = Paths.get("C:/Users/37529/IdeaProjects/Lab_7/src/com/content7/angel.png").toUri().toURL();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            //imgsrc = new File("angel.jpg").toURL().toExternalForm();
            //imgsrc= MainFrame.class.getClassLoader().getSystemResource("angel.jpg").toString();
            System.out.println(imgsrc);

            message = message.substring(0, pos) +  "<img src='file:C:/Users/37529/IdeaProjects/Lab_7/src/com/content7/angel.png' alt='' name='angel' width='74' height='85' /><br />"  + message.substring(pos+2);
            //message = message.substring(0, pos) + "<img src=\"src/com/content7/angel.webp\" width=1 height=1>"  + message.substring(pos+2);
            //message = "<html><img src=imgscr width=200height=200></img>";

        }
       /* String smile1 = "8-)";
        while(message.contains(smile1)) {
            int pos = message.indexOf(smile1);
            System.out.println(pos);
            message = message.substring(0, pos) + "<img src=\"src/com/content7/angel.png\" width=25 height=25>"  + message.substring(pos+3);
        }*/
        String html = "<span>" + message + "</span><br/>";
        incomingText.insert(0, html);
        String text  = incomingText.toString();
        System.out.println(incomingText.toString()+"\n***"+"\n");
        textAreaIncoming.setText(text);
        //textAreaIncoming.s

    }
    //"<img src=\"file:\\f:\\angel.PNG\" width=1 height=1>"
    //<img src="C:\Users\37529\IdeaProjects\Lab_7\src\com\content7\Smile.png" width=1 height=1> "<img src=\"src/com/content7/Smile.png\" width=25 height=25>"
   /* private void sendMessage() {
        try{
            // Получае мне обходимые параметры
            final String senderName = textFieldFrom.getText();
            final String destinationAddress = textFieldTo.getText();
            final String message = textAreaOutgoing.getText();
            // Убеждаемся, что поля не пустые
            if   (senderName.isEmpty()) { JOptionPane.showMessageDialog(this,
                    "Введите имя отправителя", "Ошибка", JOptionPane.ERROR_MESSAGE);
            return;
            }
            if   (destinationAddress.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Введитеадресузла-получателя", "Ошибка", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if   (message.isEmpty()) { JOptionPane.showMessageDialog(this,
                    "Введитетекстсообщения", "Ошибка", JOptionPane.ERROR_MESSAGE);
            return;
            }
            // Создаем сокет для соединения
            final Socket socket =  new Socket(destinationAddress, SERVER_PORT);
            // Открываем поток вывода данных
            final DataOutputStream out =  new DataOutputStream(socket.getOutputStream());
            // Записываем в поток имя
            out.writeUTF(senderName);
            // Записываем в поток сообщение
            out.writeUTF(message);
            // Закрываем сокет
            socket.close();
            // Помещаем сообщения в текстовую область вывода
            textAreaIncoming.append("Я -> " + destinationAddress + ": " + message + "\n");
            // Очищаем текстовую область ввода сообщения
            textAreaOutgoing.setText("");

        }catch(UnknownHostException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(MainFrame.this,"Не удалосьотправить сообщение: узел-адресат не найден",
                    "Ошибка", JOptionPane.ERROR_MESSAGE);
        } catch(IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(MainFrame.this,"Не удалось отправить сообщение",
                    "Ошибка",JOptionPane.ERROR_MESSAGE);
        }
    }*/
}