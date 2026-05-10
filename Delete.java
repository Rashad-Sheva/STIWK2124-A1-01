@DeleteMapping("/{id}")
public String deleteBook(@PathVariable Long id) {
    bookService.deleteBook(id);
    return "Book deleted successfully";
}
