package com.entity;

public class BookDetails {
    private String id, title, author, genre, photo, email;
    private String rating, price;

    public BookDetails() {

    }

    public BookDetails(String id, String title, String author, String genre, String rating, String price, String photo, String email) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.rating = rating;
        this.price = price;
        this.photo = photo;
        this.email = email;
        
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getGenre() {
        return genre;
    }

    public String getRating() {
        return rating;
    }

    public String getPrice() {
        return price;
    }
    
    public String getPhoto() {
    	return photo;
    }

	public void setId(String id) {
		this.id = id;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public void setGenre(String genre) {
		this.genre = genre;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public void setRating(String rating) {
		this.rating = rating;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String toString() {
		return "BookDetails [id=" + id + ", title=" + title + ", author=" + author + ", genre=" + genre + ", photo="
				+ photo + ", rating=" + rating + ", price=" + price + "]";
	} 
    
    
    
}
