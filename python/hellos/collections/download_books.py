import requests
from bs4 import BeautifulSoup
import os
import time


def download_top_books(num_books=10):
    """
    Download the top books from Project Gutenberg
    """
    # Create a directory to store the books
    if not os.path.exists('downloaded_books'):
        os.makedirs('downloaded_books')

    print(f"Fetching top {num_books} books from Project Gutenberg...")

    # URL for Project Gutenberg's top 100 books
    url = "https://www.gutenberg.org/browse/scores/top"

    try:
        # Get the page
        response = requests.get(url, timeout=10)
        response.raise_for_status()

        # Parse the HTML
        soup = BeautifulSoup(response.content, 'html.parser')

        # Find the top books list
        books = []
        book_links = soup.find_all('li')

        for li in book_links:
            link = li.find('a', href=True)
            if link and '/ebooks/' in link['href']:
                book_id = link['href'].split('/ebooks/')[-1]
                book_title = link.get_text(strip=True)
                books.append((book_id, book_title))

                if len(books) >= num_books:
                    break

        # Download each book
        for i, (book_id, title) in enumerate(books, 1):
            print(f"\n[{i}/{num_books}] Downloading: {title}")

            # Try to download the plain text UTF-8 version
            download_url = f"https://www.gutenberg.org/files/{book_id}/{book_id}-0.txt"

            try:
                book_response = requests.get(download_url, timeout=10)

                # If that fails, try the alternative format
                if book_response.status_code != 200:
                    download_url = f"https://www.gutenberg.org/files/{book_id}/{book_id}.txt"
                    book_response = requests.get(download_url, timeout=10)

                if book_response.status_code == 200:
                    # Clean the title for filename
                    safe_title = "".join(c for c in title if c.isalnum() or c in (' ', '-', '_')).strip()
                    safe_title = safe_title[:50]  # Limit filename length

                    filename = f"downloaded_books/{i}_{safe_title}.txt"

                    with open(filename, 'w', encoding='utf-8') as f:
                        f.write(book_response.text)

                    print(f"✓ Successfully downloaded to: {filename}")
                else:
                    print(f"✗ Failed to download (Status: {book_response.status_code})")

            except Exception as e:
                print(f"✗ Error downloading: {e}")

            # Be polite to the server
            time.sleep(1)

        print(f"\n{'=' * 60}")
        print(f"Download complete! Books saved in 'downloaded_books' folder")
        print(f"{'=' * 60}")

    except Exception as e:
        print(f"Error fetching book list: {e}")


if __name__ == "__main__":
    # Download the top 10 books
    download_top_books(10)