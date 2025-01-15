CREATE TABLE users (
    user_id UUID DEFAULT gen_random_uuid() PRIMARY KEY, -- Auto-generate UUID if not provided
	username VARCHAR(50) UNIQUE NOT NULL,
	email VARCHAR(50) UNIQUE NOT NULL,
	password VARCHAR(50) NOT NULL,
	role VARCHAR(50) CHECK (role IN ('ADMIN', 'LIBRARIAN', 'MEMBER')) NOT NULL,
	created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TYPE category_enum AS ENUM (
    -- Fiction Categories
    'MYSTERY',
    'SCIENCE_FICTION',
    'FANTASY',
    'ROMANCE',
    'HISTORICAL_FICTION',

    -- Non-Fiction Categories
    'BIOGRAPHY',
    'SELF_HELP',
    'TECHNOLOGY',
    'HISTORY',
    'SCIENCE',

    -- Academic Categories
    'MATHEMATICS',
    'PHYSICS',
    'LITERATURE',
    'PROGRAMMING',
    'PHILOSOPHY',

    -- Other Genres
    'COMICS_AND_GRAPHIC_NOVELS',
    'POETRY',
    'RELIGION_AND_SPIRITUALITY',
    'TRAVEL'
);

CREATE TABLE books (
    book_id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    isbn VARCHAR(20) UNIQUE NOT NULL,
    description TEXT,
    publication_date DATE,
    total_copies INT NOT NULL,
    available_copies INT NOT NULL,
    category category_enum NOT NULL,
    author_fullname VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE borrowings (
    borrowing_id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    user_id UUID REFERENCES users(user_id),
    book_id UUID REFERENCES books(book_id),
    borrow_date DATE NOT NULL,
    due_date DATE NOT NULL,
    return_date DATE,
    status VARCHAR(20) CHECK (status IN ('BORROWED', 'RETURNED', 'OVERDUE')) NOT NULL
);

