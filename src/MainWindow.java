import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextArea;

public class MainWindow extends JFrame implements ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4880718883699230100L;
	private static final String KEYFACTORY_INSTANCE = "RSA";
	private static final String CIPHER_INSTANCE = "RSA/ECB/PKCS1Padding";
	
	private JTextArea encriptedText;
	private JTextArea decriptedText;
	private JTextArea publicKey;
	private JTextArea privateKey;
	
	private JButton generateRSAKeysButton;
	private JButton encryptTextPrivateButton;
	private JButton decryptTextPublicButton;
	private JButton decryptTextPrivateButton;
	private JButton encryptTextPublicButton;

	MainWindow(String title) {
		super(title);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setPreferredSize(new Dimension(800, 600));
		
		int textAreaWidth = (this.getPreferredSize().width / 2) - 80;
		int textAreaHeight = (this.getPreferredSize().height / 2) - 30;
		
		encriptedText = new JTextArea();
		encriptedText.setPreferredSize(new Dimension(textAreaWidth, textAreaHeight));
		encriptedText.setBorder(BorderFactory.createLineBorder(Color.gray, 2));
		encriptedText.setText("encriptedText");
		decriptedText = new JTextArea();
		decriptedText.setPreferredSize(new Dimension(textAreaWidth, textAreaHeight));
		decriptedText.setBorder(BorderFactory.createLineBorder(Color.gray, 2));
		decriptedText.setText("MethAtroN");
		
		generateRSAKeysButton = new JButton("Gen. RSA Keys");
		generateRSAKeysButton.setActionCommand("generateRSAKeysbutton");
		generateRSAKeysButton.addActionListener(this);
		decryptTextPublicButton = new JButton("Decrypt Public");
		decryptTextPublicButton.setActionCommand("decryptTextPublic");
		decryptTextPublicButton.addActionListener(this);
		encryptTextPrivateButton = new JButton("Encrypt Private");
		encryptTextPrivateButton.setActionCommand("encryptTextPrivate");
		encryptTextPrivateButton.addActionListener(this);
		decryptTextPrivateButton = new JButton("Decrypt Private");
		decryptTextPrivateButton.setActionCommand("decryptTextPrivate");
		decryptTextPrivateButton.addActionListener(this);
		encryptTextPublicButton = new JButton("Encrypt Public");
		encryptTextPublicButton.setActionCommand("encryptTextPublic");
		encryptTextPublicButton.addActionListener(this);
		
		privateKey = new JTextArea();
		privateKey.setPreferredSize(new Dimension(textAreaWidth, textAreaHeight));
		privateKey.setBorder(BorderFactory.createLineBorder(Color.gray, 2));
		privateKey.setText("privateKey");
		publicKey = new JTextArea();
		publicKey.setPreferredSize(new Dimension(textAreaWidth, textAreaHeight));
		publicKey.setBorder(BorderFactory.createLineBorder(Color.gray, 2));
		publicKey.setText("publicKey");
		
		JPanel textContainer = new JPanel(new BorderLayout(5, 5));
		textContainer.add(encriptedText, BorderLayout.NORTH);
		textContainer.add(decriptedText, BorderLayout.SOUTH);
//		textContainer.setOpaque(true);
//		textContainer.setBackground(Color.red);
		
		JPanel buttonContainer = new JPanel();
		buttonContainer.setLayout(new BoxLayout(buttonContainer, BoxLayout.Y_AXIS));
		buttonContainer.add(generateRSAKeysButton);
		buttonContainer.add(new JSeparator());
		buttonContainer.add(encryptTextPrivateButton);
		buttonContainer.add(decryptTextPublicButton);
		buttonContainer.add(new JSeparator());
		buttonContainer.add(encryptTextPublicButton);
		buttonContainer.add(decryptTextPrivateButton);
//		buttonContainer.setOpaque(true);
//		buttonContainer.setBackground(Color.green);
		
		JPanel keyContainer = new JPanel(new BorderLayout(5, 5));
		keyContainer.add(privateKey, BorderLayout.NORTH);
		keyContainer.add(publicKey, BorderLayout.SOUTH);
//		keyContainer.setOpaque(true);
//		keyContainer.setBackground(Color.blue);
		
		this.getContentPane().setLayout(new FlowLayout());
		this.getContentPane().add(textContainer);
		this.getContentPane().add(buttonContainer);
		this.getContentPane().add(keyContainer);		
		
		this.pack();
	}
	
	@Override
	public void actionPerformed(ActionEvent ae) {
		switch (ae.getActionCommand()) {
		/*
		 * generateRSAKeysbutton - Gen. RSA Keys
		 * encryptTextPrivate - Encrypt Private
		 * decryptTextPublic - Decrypt Public
		 * encryptTextPublic - Encrypt Public
		 * decryptTextPrivate - Decrypt Private
		 */
		case "generateRSAKeysbutton":
			try {
				KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
				kpg.initialize(2048);
				KeyPair kp = kpg.generateKeyPair();
				privateKey.setText(
						"-----BEGIN RSA PRIVATE KEY-----\n" +
						Base64.getMimeEncoder().encodeToString(kp.getPrivate().getEncoded())+
						"\n-----END RSA PRIVATE KEY-----\n"
						);
				System.err.println("Private key format: " + kp.getPrivate().getFormat());
				publicKey.setText(
						"-----BEGIN RSA PUBLIC KEY-----\n" +
						Base64.getMimeEncoder().encodeToString(kp.getPublic().getEncoded())+
						"\n-----END RSA PUBLIC KEY-----\n"
						);
				System.err.println("Public key format: " + kp.getPublic().getFormat());
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case "encryptTextPrivate":
			try {
				encriptedText.setText(encrypt(decriptedText.getText(), getPrivaKey(privateKey.getText())));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case "decryptTextPublic":
			try {
				decriptedText.setText(decrypt(encriptedText.getText(), getPublicKey(publicKey.getText())));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case "encryptTextPublic":
			try {
				encriptedText.setText(encrypt(decriptedText.getText(), getPublicKey(publicKey.getText())));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case "decryptTextPrivate":
			try {
				decriptedText.setText(decrypt(encriptedText.getText(), getPrivaKey(privateKey.getText())));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		default:
            System.out.println(ae.getActionCommand() + " - " + ((JButton)ae.getSource()).getText());
			break;
		}
	}
	
	private static String encrypt(String input, Key key)
			throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException,
			IllegalBlockSizeException, BadPaddingException
	{
		Cipher cipher = Cipher.getInstance(CIPHER_INSTANCE);
		cipher.init(Cipher.ENCRYPT_MODE, key);
		return Base64.getMimeEncoder().encodeToString(cipher.doFinal(input.getBytes()));		
	}
	
	private static String decrypt(String input, Key key)
			throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException,
			IllegalBlockSizeException, BadPaddingException
	{
		Cipher cipher = Cipher.getInstance(CIPHER_INSTANCE);
		cipher.init(Cipher.DECRYPT_MODE, key);
		return new String(cipher.doFinal(Base64.getMimeDecoder().decode(input)));		
	}
	
	private static Key getPrivaKey(String key)
			throws NoSuchAlgorithmException, InvalidKeySpecException
	{
		String keyString = key.replace("-----BEGIN RSA PRIVATE KEY-----\n", "").replace("\n-----END RSA PRIVATE KEY-----\n", "");
		byte[] keyBytes = Base64.getMimeDecoder().decode(keyString);
		PKCS8EncodedKeySpec ks = new PKCS8EncodedKeySpec(keyBytes);
		KeyFactory kf = KeyFactory.getInstance("RSA");
		return kf.generatePrivate(ks);
	}
	
	private static Key getPublicKey(String key)
			throws NoSuchAlgorithmException, InvalidKeySpecException
	{
		String keyString = key.replace("-----BEGIN RSA PUBLIC KEY-----\n", "").replace("\n-----END RSA PUBLIC KEY-----\n", "");
		byte[] keyBytes = Base64.getMimeDecoder().decode(keyString);
		X509EncodedKeySpec ks = new X509EncodedKeySpec(keyBytes);
		KeyFactory kf = KeyFactory.getInstance("RSA");
		return kf.generatePublic(ks);
	}
}