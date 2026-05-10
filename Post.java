@PostMapping
public Book createBook(@RequestBody Book book) {
    return bookService.createBook(book);
}
