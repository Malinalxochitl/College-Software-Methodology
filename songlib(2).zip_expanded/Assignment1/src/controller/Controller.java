package controller;

//
//Assignment 1 - Song Library
//Jin Lee (jl1795)
//Joel Kurian (jjk267)
//

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.util.Callback;
import model.Song;

public class Controller {
	
	@FXML private Button add;
	@FXML private Button edit;
	@FXML private Button delete;
	@FXML private Button done;
	@FXML private Button cancel;
	
	@FXML private Label titleLabel;
	@FXML private Label artistLabel;
	@FXML private Label albumLabel;
	@FXML private Label yearLabel;
	
	@FXML private ListView<Song> listview;
	@FXML private TextField titleText;
	@FXML private TextField artistText;
	@FXML private TextField albumText;
	@FXML private TextField yearText;
	
	private boolean editing;
	private boolean adding;
	private Song selectedSong;
	
	ObservableList<Song> songs = FXCollections.observableArrayList();
	
    @FXML private ResourceBundle resources;
    @FXML private URL location;

 
 
    private ObservableList<Song> obsList;
    
    @FXML
    public void onButton(ActionEvent e) {
    	
    	if (e.getSource() == add){
    		addMode( true );
    		return;
    	}
    	else if (e.getSource() == delete){
    		Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this song?", ButtonType.YES, ButtonType.CANCEL);
    		alert.showAndWait();
    		if(alert.getResult() != ButtonType.YES) {
    			return;
    		}
    		int i = listview.getSelectionModel().getSelectedIndex();
    		//System.out.print(i);
    		
			titleLabel.setText(null);
			artistLabel.setText(null);
			yearLabel.setText(null);
			albumLabel.setText(null);
			songs.remove(selectedSong);
			if(songs.size() == 0) {
				edit.setDisable(true);
				delete.setDisable(true);
			}
			save();
			if (i == 0) {
				listview.getSelectionModel().select(0);
			}
			else if (songs.size() == 1){
				listview.getSelectionModel().selectFirst();
			}
			else {
				listview.getSelectionModel().selectNext();
			}
			
    		return;
    	}
    	else if (e.getSource() == edit){
    		editMode( true );
    		return;
    	} 
    	else if (e.getSource() == cancel){
			editMode( false );
			addMode( false );
    		return;
    	}
    	else if (e.getSource() == done){

			if (editing) {
				String name = titleText.getText();
				String artist= artistText.getText();

				if (name!=null&& !name.isEmpty()&& artist!=null && !artist.isEmpty()){
					Song song = new Song(titleText.getText(), artistText.getText(),
							albumText.getText(), yearText.getText());

					if (duplicate(song)){
						//duplicate song
						Alert alert = new Alert(AlertType.INFORMATION);
						alert.setTitle("Information Dialog");
						alert.setHeaderText("Song already exists!");
						alert.setContentText("Duplicated songs are not allowed");
						alert.showAndWait();

					}
					else {
						songs.remove(selectedSong);
						songs.add(song);
						FXCollections.sort(songs);
						listview.getSelectionModel().select(song);
						editMode( false );
						save();
					}

				}
				else {
					//empty fields
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("Information Dialog");
					alert.setHeaderText("Fields are empty!");
					alert.setContentText("Please enter a title and an artist.");
					alert.showAndWait();
				}
			}
			else if (adding) {
				String name = titleText.getText();
				String artist = artistText.getText();

				if(name != null && !name.isEmpty() && artist != null && !artist.isEmpty()) {
					Song song = new Song(titleText.getText(), artistText.getText(),
							albumText.getText(), yearText.getText());
					
					if(duplicate(song)) {
						
						Alert alert = new Alert(AlertType.INFORMATION);
						alert.setTitle("Information Dialog");
						alert.setHeaderText("Song already exists!");
						alert.setContentText("Duplicated songs are not allowed");
						alert.showAndWait();
					}
					else {
						songs.add(song);
						FXCollections.sort(songs);
						listview.getSelectionModel().select(song);
						addMode(false);
						save();
					}
				}
				else {
					
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("Information Dialog");
					alert.setHeaderText("Fields are empty!");
					alert.setContentText("Please enter a title and an artist.");
					alert.showAndWait();
				}
			}
    		return;
    	}  	
    }
   
    
    @FXML
    public void editMode( boolean b ){
    	if ( b == true){
    		if (!editing && !adding ){
	    		editing = true;
	    		edit.setDisable(true);
				titleText.setText(selectedSong.getTitle());
				artistText.setText(selectedSong.getArtist());
				albumText.setText(selectedSong.getAlbum());
				yearText.setText(selectedSong.getYear());
	    		fieldsOn(b);
    		}    		
    	} else {
    		if ( editing || adding ){
    			editing = false;
    			edit.setDisable(false);
    			fieldsOn(b);
    		}
    	}
    	return;
    }
    
    @FXML
    public void addMode ( boolean b ){
    	if (b == true){	
    		if (!editing && !adding ){
    			adding = true;
				titleText.setText(null);
				artistText.setText(null);
				albumText.setText(null);
				yearText.setText(null);
    			fieldsOn(b);
    		}
    	} else {
    		if (editing || adding ){
    			adding = false;
    			
    			fieldsOn(b);
    		}
    	}
    	return;
    }
    
    @FXML
    public void fieldsOn( boolean b ) {
    	if (b == true ) {
    		titleLabel.setVisible(false);
    		artistLabel.setVisible(false);
    		albumLabel.setVisible(false);
    		yearLabel.setVisible(false);
    		
    		titleText.setVisible(true);	
    		artistText.setVisible(true);
    		albumText.setVisible(true);
    		yearText.setVisible(true);
    		
    		titleText.setEditable(true);	//make fields editable
    		artistText.setEditable(true);
    		albumText.setEditable(true);
    		yearText.setEditable(true);
    		
    		titleText.setDisable(false);	//enable fields
    		artistText.setDisable(false);
    		albumText.setDisable(false);
    		yearText.setDisable(false);
    		
    		add.setDisable(true);		//disable add/del/edit buttons
    		delete.setDisable(true);
    		edit.setDisable(true);
    		
    		done.setDisable(false);		//enable done/cancel buttons
    		done.setDefaultButton(true);
    		cancel.setDisable(false);
    		cancel.setCancelButton(true);
    		
    		listview.setMouseTransparent( true );	//disable song list
    		listview.setFocusTraversable( false );
    		listview.setDisable(true);
    		
    	} else {
    		titleLabel.setVisible(true);		//make labels visible
    		artistLabel.setVisible(true);
    		albumLabel.setVisible(true);
    		yearLabel.setVisible(true);
			
			titleText.setVisible(false);	//make fields invisible
    		artistText.setVisible(false);
    		albumText.setVisible(false);
    		yearText.setVisible(false);
			
    		titleText.setEditable(false);	//make fields uneditable
    		artistText.setEditable(false);
    		albumText.setEditable(false);
    		yearText.setEditable(false);
    		
    		titleText.setDisable(true);		//disable fields
    		artistText.setDisable(true);
    		albumText.setDisable(true);
    		yearText.setDisable(true);
    		
    		add.setDisable(false);		//enable add/del/edit buttons
    		delete.setDisable(false);
    		edit.setDisable(false);
    		
    		done.setDisable(true);		//disable done/cancel buttons
    		done.setDefaultButton(false);
    		cancel.setDisable(true);
    		cancel.setCancelButton(false);
    		
    		listview.setMouseTransparent( false );	//enable song list
    		listview.setFocusTraversable( true );
    		listview.setDisable(false);
    	}
    	return;
    }

	public void setSelected(Song song) {
		if(song != null) {
			selectedSong = song;
			titleLabel.setText(selectedSong.getTitle());
			artistLabel.setText(selectedSong.getArtist());
			albumLabel.setText(selectedSong.getAlbum());
			yearLabel.setText(selectedSong.getYear());
			titleText.setText(selectedSong.getTitle());
			artistText.setText(selectedSong.getArtist());
			albumText.setText(selectedSong.getAlbum());
			yearText.setText(selectedSong.getYear());
			edit.setDisable(false);
			delete.setDisable(false);
		}
	}

	public boolean duplicate(Song newSong) {
		for(Song song : songs) {
			
			if(song.compareTo(newSong)==0) {
				return true;
			}
			
		}

		return false;
	}

    @FXML
    void initialize() {
    	editing = false;
    	
    	titleLabel.setText(null);
		artistLabel.setText(null);
		yearLabel.setText(null);
		albumLabel.setText(null);
		
		listview.setItems(obsList);
		
		listview.setCellFactory(new Callback<ListView<Song>, ListCell<Song>>(){
			
			@Override
			
			public ListCell<Song> call(ListView<Song> p) {
				
				ListCell<Song> cell = new ListCell<Song>(){
					
					@Override
					protected void updateItem(Song s, boolean bln) {
						super.updateItem(s, bln);
						if (s != null) {
							setText(s.getDisplay());
						}
						else if (s == null){
							setText(null);	
						}
						
					}
				
				};
				return cell;
				
			}
			
		});
    	
    	listview.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Song>(){
			@Override
			public void changed(ObservableValue<? extends Song> observable, Song oldValue, Song newValue) {
				setSelected(newValue);
			}
    	});
    	
    	File f = new File("songs.csv");
		if (!f.exists()){
			try {
				f.createNewFile();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
    	
    	String File = "songs.csv";
    	BufferedReader br = null;
    	String line = "";
    	String SplitBy = ",";

    	try {
    		
    		br = new BufferedReader(new FileReader(File));
    		
    		while ((line = br.readLine()) != null) {
				String[] songDetails = new String[4];
				String[] temp = line.split(SplitBy);	//create array of song separated by comma

				for(int i = 0; i < temp.length; i++) {
					songDetails[i] = temp[i];
				}

				Song song = new Song(songDetails[0], songDetails[1], songDetails[2], songDetails[3]);
    			songs.add(song);
    		}
    		
    		FXCollections.sort(songs);
    		listview.setItems(songs);
    		
    		//selecting the first item
    		if (!songs.isEmpty()){
    			listview.getSelectionModel().select(0);
    		}
    	

    	} catch (FileNotFoundException e) {
    		e.printStackTrace();
    	} catch (IOException e) {
    		e.printStackTrace();
    	} finally {
    		if (br != null) {
    			try {
    				br.close();
    			} catch (IOException e) {
    				e.printStackTrace();
    			}
    		}
    	}
    }

	public void save() {
		String csvFile = "songs.csv";
		BufferedWriter writer = null;
		
		try{
			
			writer = new BufferedWriter(new FileWriter(csvFile));


			for(int i = 0; i < songs.size(); i++) {
				Song song = songs.get(i);
				String album = song.getAlbum();
				String year = song.getYear();

				if(album == null) {
					album = "";
				}

				if(year == null) {
					year = "";
				}

				writer.write(song.getTitle() + "," + song.getArtist() + "," + album + "," + year + "\n" );
			}
		}
		catch ( IOException e){
		}
		finally{
			
			try{
				if ( writer != null)
					writer.close( );
			}
			
			catch ( IOException e){
			}
		}
	}

}