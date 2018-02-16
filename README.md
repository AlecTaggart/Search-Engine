# Search Engine

Submission for the CS 212 Search Engine project.

USAGE:

-path path where the flag -path indicates the next argument is a path to either a single HTML file or a directory of HTML files that must be processed and added to the inverted index

-index path where the flag -index is an optional flag that indicates the next argument is the path tto use for the inverted index output file. If the path argument is not provided, use index.json as the default output path. If the -index flag is not provided, do not produce an output file.

-query filepath where -query indicates the next argument is a path to a text file of queries to be used for search. If this flag is not provided, then no search should be performed.

-exact where -exact indicates all search operations performed should be exact search. If the flag is NOT present, any search operations should be partial search instead.

-results filepath where -results is an optional flag that indicates the next argument is a file path, and filepath is the path to the file to use for the search results output file. This may include partial or exact search results! If the filepath argument is not provided, use results.json as the default output filename. If the -results flag is not provided, do not produce an output file of search results but still perform the search operation.

-threads num threads where -threads indicates the next argument num is the number of threads to use. If an invalid number of threads are provided, please default to 5 threads. If the -threads flag is not provided, assume no threads.

-url seed where -url indicates the next argument seed is the seed URL the web crawler should initially crawl to build the inverted index.

-limit total where -limit indicates the next argument total is the total number of URLs to crawl (including the seed URL) when building the index. Use 50 as the default value if this flag is not properly provided.

-port num where -port indicates the next argument is the port the web server should use to accept socket connections. Use 8080 as the default value if it is not provided.


FUNCTION:


-Process command-line arguments to determine the input to process and output to produce, determine the file to parse for queries, 

-Create a custom thread safe inverted index data structure that stores a mapping from a word to the HTML file(s) the word was found, and the position(s) in that file the word is located. The positions should start at 1. This will require nesting multiple built-in data structures.

-If provided a directory as input, must find all files within that directory and all subdirectories and parse each HTML file found. If provided a single HTML file as input, only parse that individual file.

-Use the UTF-8 character encoding for all file processing, including reading and writing.

-In the raw HTML, replace any HTML comments, head, style, or script sections, remaining HTML tags, and HTML entities with a single space character. In the remaining text, replace any non-alphabetic character (such as punctuation like ? or digits like 41) with a single space. In the remaining text, convert all remaining characters to lowercase, split the text into individual words by spaces, and store each word in an inverted index along with the normalized relative path to the original HTML file.

-If the appropriate command-line arguments are provided, output the inverted index in pretty JSON format.

-Efficiently return partial / exact search results from inverted index.

-Sort the search results using a simple metric for relevance. Frequency / Position / Location

-Supports multiple word search queries, where search results from individual query words are combined together. 

-Clean and store the queries processed from the query file in a consistent format.

-Creates a custom read/write lock class that allows multiple concurrent read operations, but non-concurrent write and read/write operations.

-Uses a work queue to build inverted index from a directory of files using multiple worker threads. Each worker thread parses a single HTML file.

-Uses a work queue to search inverted index from a file of multiple word queries (for both exact and partial search). Each worker thread handles an individual query.

-Exits gracefully without calling System.exit() when all of the building and searching operations are complete.

-The web crawler uses a breadth-first approach to crawling URLs and uses sockets to download webpages over HTTP when crawling. 

-Search Displays a webpage with a text box where users may enter a multi-word search query, and a button that submits the query to the search engine. Also provides addional functionality: search history, visited results, private search, time stamps, last log, user accounts (username password data), addional web crawls. 

