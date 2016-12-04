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

public class Answers extends JFrame {

	private static final long serialVersionUID = -6846315193410091203L;

	private JTextArea _editor;
	private JTable _table;
	private ResultSetTableModel _tableModel;
	private JButton AddAnswer;
	private JButton RankUp;
	private JButton RankDown;

	int idQuestion;

	public Answers(int idQuestion) {
		super("Answers");
		setSize(1000, 800);
		setLocationRelativeTo(null);
		this.idQuestion = idQuestion;
		initGUI();
		refreshAnswers();
	}

	private void initGUI() {
		getContentPane().setLayout(new GridLayout(5, 2));

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

		AddAnswer = new JButton("Dodaj odpowiedŸ do tego pytania");
		AddAnswer.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Object source = e.getSource();
				if (source == AddAnswer) {
					int max = maxAIO() + 1;

					String query = "INSERT INTO odpowiedzi ( id , odpowiedz , rank , idp ) VALUES ( "
							+ max
							+ " , '"
							+ _editor.getText()
							+ "', 0 , "
							+ idQuestion + "  ) ;";
					sendQuery(query);

					refreshAnswers();
				}
			}
		});

		add(AddAnswer);

		RankUp = new JButton("Zwiêksz rank");
		RankUp.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Object source = e.getSource();
				if (source == RankUp) {
					int row = _table.getSelectedRow();
					if (row >= 0) {
						int idAnswer = (Integer) _table.getValueAt(row, 0);

						int newRank = giveRank(idAnswer) + 1;

						String query = "UPDATE odpowiedzi SET rank = "
								+ newRank + " WHERE id =" + idAnswer;
						sendQuery(query);

						refreshAnswers();
					}

				}
			}
		});

		add(RankUp);

		RankDown = new JButton("Zmniejsz rank");
		RankDown.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Object source = e.getSource();
				if (source == RankDown) {
					int row = _table.getSelectedRow();
					if (row >= 0) {
						int idAnswer = (Integer) _table.getValueAt(row, 0);

						int newRank = giveRank(idAnswer) - 1;

						String query = "UPDATE odpowiedzi SET rank = "
								+ newRank + " WHERE id =" + idAnswer;
						sendQuery(query);

						refreshAnswers();
					}
				}
			}
		});

		add(RankDown);

	}

	int giveRank(int id) {
		int rank = 0;
		try {
			Statement stmt = DBManager.getConnection().createStatement();
			ResultSet queryResult = stmt
					.executeQuery("select rank from odpowiedzi where id = "
							+ id + ";");
			queryResult.next();
			rank = queryResult.getInt("rank");
			queryResult.close();
			stmt.close();
		} catch (Exception e) {
			_editor.setText(e.getMessage());
		}
		return rank;
	}

	int maxAIO() {
		int id = 0;
		try {
			Statement stmt = DBManager.getConnection().createStatement();
			ResultSet queryResult = stmt
					.executeQuery("select id from odpowiedzi where id = (select max(id) from odpowiedzi );");
			queryResult.next();
			id = queryResult.getInt("id");
			queryResult.close();
			stmt.close();
		} catch (Exception e) {
			_editor.setText(e.getMessage());
		}
		return id;
	}

	void refreshAnswers() {

		sendQuery("select * from odpowiedzi where idp=" + idQuestion);

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