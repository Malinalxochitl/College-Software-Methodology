package model;

//
//Assignment 1 - Song Library
//Jin Lee (jl1795)
//Joel Kurian (jjk267)
//

public class Song implements Comparable<Song> {
	   private String title;
	   private String artist;
	   private String album;
	   private String year;

	   public Song(String title, String artist, String album, String year) {
	      this.title = title;
	      this.artist = artist;
	      this.album = album;
	      this.year = year;
	   }
	   
	   public String getDisplay() {
		   return title + " / " + artist;
	   }

	   public String getTitle() {
	      return title;
	   }
	   
	   public String getArtist() {
		   return artist;
	   }
	   
	   public String getAlbum() {
		   return album;
	   }
	   
	   public String getYear() {
		   return year;
	   }

	   public void setName(String title) {
	      this.title = title;
	   }

	   public void setArtist(String artist) {
	      this.artist = artist;
	   }

	   public void setAlbum(String album) {
	      this.album = album;
	   }

	   public void setYear(String year) {
	      this.year = year;
	   }

		@Override
		public int compareTo(Song song) {
			int result = this.getTitle().compareToIgnoreCase(song.getTitle());
			if (result > 0) {
				return 1;
			} else if (result < 0) {
				return -1;
			} else {
				result = this.getArtist().compareToIgnoreCase(song.getArtist());
				if (result > 0) {
					return 1;
				} else if (result < 0) {
					return -1;
				} else {
					return 0;
				}
			}
		}
	   
	   
	   
	   @Override
	   public boolean equals(Object o) {
	      if(this == o) {
	         return true;
	      }
	      if(o == null || getClass() != o.getClass()) {
	         return false;
	      }

	      Song song = (Song) o;
	      return title.equals(song.title) && artist.equals(song.artist);
	   }

	   @Override
	   public String toString() {
	      return title;
	   }
	}
