import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.constant.ConstantDesc;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

class Server extends JFrame {

    ServerSocket server;
    Socket socket;

    BufferedReader br;
    PrintWriter out;

    // declare components
    private JLabel heading = new JLabel("Server Area");
    private JTextArea messageArea = new JTextArea();
    private JTextField messageInput = new JTextField();

    private Font font = new Font("Roboto", Font.PLAIN, 20);

    public Server() {
        try {
            server = new ServerSocket(7777);
            System.out.println("Server is ready to accept connection!");
            System.out.println("Waiting.....");
            socket = server.accept();

            br = new BufferedReader(new InputStreamReader(
                    socket.getInputStream()));

            out = new PrintWriter(socket.getOutputStream());
            createGui();
            handleEvents();
            startReading();
            // startWriting();
        } catch (Exception e) {

            e.printStackTrace();
        }

    }

    private void handleEvents() {

        messageInput.addKeyListener(new KeyListener() {

            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {

            }

            @Override
            public void keyReleased(KeyEvent e) {
                // System.out.println("Key released: " + e.getKeyCode());
                if (e.getKeyCode() == 10) {
                    String contentToSend = messageInput.getText();
                    messageArea.append("Me: " + contentToSend + "\n");
                    out.println(contentToSend);
                    out.flush();
                    messageInput.setText("");
                    messageInput.requestFocus();
                }

            }

        });

    }

    private void createGui() {
        // gui code
        this.setTitle("Server Messenger[END]");
        this.setSize(600, 700);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // coding for component
        heading.setFont(font);
        messageArea.setFont(font);
        messageInput.setFont(font);
        // heading.setIcon(new ImageIcon("chat.jpg"));
        ImageIcon imageIcon = new ImageIcon("chat.jpg");// assign image
        Image image = imageIcon.getImage();// transform it
        Image newimg = image.getScaledInstance(90, 90, java.awt.Image.SCALE_SMOOTH);// scale it smoothly

        heading.setIcon(new ImageIcon(newimg));// to a new ImageIcon

        heading.setHorizontalAlignment(SwingConstants.CENTER);
        heading.setVerticalTextPosition(SwingConstants.BOTTOM);
        heading.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        messageArea.setEditable(false);
        messageInput.setHorizontalAlignment(SwingConstants.CENTER);

        // Layout of frame
        this.setLayout(new BorderLayout());

        // adding the components to the frame
        this.add(heading, BorderLayout.NORTH);
        JScrollPane jScrollPane = new JScrollPane(messageArea);
        this.add(jScrollPane, BorderLayout.CENTER);
        this.add(messageInput, BorderLayout.SOUTH);

        this.setVisible(true);

    }

    public void startReading() {

        // thread that keeps reading the date

        Runnable r1 = () -> {
            System.out.println("Reader Started... ");

            try {
                while (true) {
                    String msg = br.readLine();

                    if (msg.equals("exit")) {
                        System.out.println("Client terminated the chat! ");
                        JOptionPane.showMessageDialog(this, "Server Terminated the chat.");
                        messageInput.setEnabled(false);
                        break;
                    }
                    messageArea.append("Client: " + msg + "\n");
                    // System.out.println("Client: " + msg);
                }
            } catch (Exception e) {
                e.printStackTrace();

            }

        };
        new Thread(r1).start();
    }

    public void startWriting() {
        System.out.println("Writer Started...");
        Runnable r2 = () -> {
            while (true) {
                try {
                    BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));
                    String content = br1.readLine();

                    out.println(content);
                    out.flush();
                    // System.out.println("Server: " + content);
                } catch (Exception e) {
                    e.printStackTrace();

                }
            }
        };
        new Thread(r2).start();

    }

    // thread that take data from user and send it to client

    public static void main(String[] args) {
        new Server();
    }

}