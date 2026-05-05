@PutMapping("/{id}")
public Book updateBook(@PathVariable Long id,
                       @RequestBody Book book) {
    return bookService.updateBook(id, book);
}
