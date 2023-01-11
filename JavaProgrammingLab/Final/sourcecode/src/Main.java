import javax.swing.JDialog;

public class Main {
	public static void main(String[] args) {
		UserLoginForm loginForm = new UserLoginForm();
		loginForm.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		loginForm.setVisible(true);
	}
}