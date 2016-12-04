package pl.projectdcm.gui;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;

import pl.projectdcm.data.DBManager;
import pl.projectdcm.data.ResultSetTableModel;

public class QuestionsMenu extends JFrame {

	private static final long serialVersionUID = -6846315193410091203L;

	public static void main(String[] args) {
		JFrame app_frame = new QuestionsMenu();
		/*Staff staff = new Staff();
		staff.addSampleEntries();*/
		app_frame.setVisible(true);
	}

	private JTextArea _editor;
	private JButton _sendButton;
	private JTable _table;
	private ResultSetTableModel _tableModel;
	private JButton AddQuestion;
	private JButton ShowAllQuestion;
	private JButton ShowAnswers;

	public QuestionsMenu() {
		super("Question and Answers");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(1100, 900);
		setLocationRelativeTo(null);
		initGUI();
		sendQuery("select * from pytania");
	}

	private void initGUI() {
		getContentPane().setLayout(new GridLayout(6, 2));

		// query area
		_editor = new JTextArea("");
		_editor.setFont(new Font("Courier New", 0, 12));
		_editor.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		JScrollPane editorScroll = new JScrollPane();
		editorScroll.setViewportView(_editor);
		editorScroll.setPreferredSize(new Dimension(1, 100));
		add(editorScroll);

		// data table
		_tableModel = new ResultSetTableModel(null);
		_table = new JTable(_tableModel);
		JScrollPane tableScroll = new JScrollPane(_table);
		add(tableScroll);

		// button
		_sendButton = new JButton("Send query");
		add(_sendButton);

		AddQuestion = new JButton("Dodaj Pytanie");
		AddQuestion.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Object source = e.getSource();
				if (source == AddQuestion) {
					int max = maxAID() + 1;

					String query = "INSERT INTO pytania ( id , pytanie )VALUES ( "
							+ max + " , '" + _editor.getText() + "' ) ;";
					sendQuery(query);

					String query2 = "select * from pytania";
					sendQuery(query2);
				}
			}
		});
		add(AddQuestion);

		ShowAllQuestion = new JButton("Odswiez liste pytan");
		ShowAllQuestion.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Object source = e.getSource();
				if (source == ShowAllQuestion) {				
					sendQuery("select * from pytania");
				}
			}
		});

		add(ShowAllQuestion);

		
		
		
		ShowAnswers = new JButton("Pokaz Odpowiedzi do pytania");
		ShowAnswers.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Object source = e.getSource();
				if (source == ShowAnswers) {
					 int row = _table.getSelectedRow();
                     if (row >= 0) {
                             int idQuestion = (Integer) _table.getValueAt(row, 0);										
					showAnswers(idQuestion);}
										
				}
			}
		});

		add(ShowAnswers);
				
				
		
		
		// send query handler
		_sendButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String query = _editor.getText();
				sendQuery(query);

			}
		});
	}

	
	void showAnswers(int idQuestion){
		JFrame app_frame2 = new Answers(idQuestion);
		app_frame2.setVisible(true);
		
			
		
	}
	
	
	int maxAID() {
		int id = 0;
		try {
			Statement stmt = DBManager.getConnection().createStatement();
			ResultSet queryResult = stmt
					.executeQuery("select id from pytania where id = (select max(id) from pytania);");
			queryResult.next();
			id = queryResult.getInt("id");
			queryResult.close();
			stmt.close();
		} catch (Exception e) {
			_editor.setText(e.getMessage());
		}
		return id;
	}
	

	protected void sendQuery(String query) {
		try {
			// read query from the editor
			// String query = _editor.getText();

			if (query.length() > 6
					&& (query.substring(0, 6).equalsIgnoreCase("insert")
							|| query.substring(0, 6).equalsIgnoreCase("update") || query
							.substring(0, 6).equalsIgnoreCase("delete"))) {
				Statement stmt = DBManager.getConnection().createStatement();
				stmt.executeUpdate(query);
				_tableModel.setResultSet(null);
				stmt.close();
			} else if ((query.length() > 6)
					&& (query.substring(0, 6).equalsIgnoreCase("select"))) {
				// create SQL statement and execute query read from the _editor
				Statement stmt = DBManager.getConnection().createStatement();
				ResultSet queryResult = stmt.executeQuery(query);
				// pass the resultSet to the table model (use setResultSet
				// method from the model)

				_tableModel.setResultSet(queryResult);

				// close the resultSet and statement
				queryResult.close();
				stmt.close();
			} else {
				Statement stmt = DBManager.getConnection().createStatement();
				stmt.execute(query);
				stmt.close();
			}
		} catch (Exception e) {
			_editor.setText(e.getMessage());
		}
	}
}