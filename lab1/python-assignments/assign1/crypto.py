#!/usr/bin/env python3 -tt
"""
File: crypto.py
---------------
Assignment 1: Cryptography
Course: CS 41
Name: <YOUR NAME>
SUNet: <SUNet ID>

Replace this with a description of the program.
"""
import utils

# Caesar Cipher
abc = ['A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J',
           'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T',
           'U', 'V', 'W', 'X', 'Y', 'Z']

def index_back(ind):

    if ind < 23:
        ind = ind + 3
    else:
        ind = ind - 23

    return ind

def caesar_error(plaintext):
    if len(plaintext) == 0:
        raise ValueError("Text cannot be empty")
     
    for letter in plaintext:
        if letter.isalpha() and letter.isupper() == False:
            raise ValueError("Text must contain only uppercase letters")

def encrypt_caesar(plaintext):
    """Encrypt plaintext using a Caesar cipher.

    Add more implementation details here.
    """
    caesar_error(plaintext)

    ciphertext = ""

    for letter in plaintext:
        if letter in abc:
            letter_index = abc.index(letter)
            ciphertext = ciphertext + abc[index_back(letter_index)]
        else:
            ciphertext += letter

    return ciphertext

def index_before(ind):

    if ind > 2:
        ind = ind - 3
    else:
        ind = 23 + ind

    return ind


def decrypt_caesar(ciphertext):
    """Decrypt a ciphertext using a Caesar cipher.

    Add more implementation details here.
    """
    caesar_error(ciphertext)
    
    plaintext = ""
    
    for letter in ciphertext:
        if letter in abc:
            letter_index = abc.index(letter)
            plaintext = plaintext + abc[index_before(letter_index)]
        else:
            plaintext += letter
    
    return plaintext


# Vigenere Cipher

def keyword_repeat(keyword, length):

    new_key = ""

    if length > len(keyword):
        part = length % len(keyword)
        whole = length // len(keyword) 

        for i in range(whole):
            new_key += keyword

        new_key += keyword[0:part]
    else:
        new_key = keyword

    return new_key

def vigenere_error(plaintext, keyword):
    if len(plaintext) == 0:
                 raise ValueError("Text cannot be empty")
         
    for letter in plaintext:
             if letter.isalpha() == False or letter.isupper() == False:
                     raise ValueError("Text must contain only uppercase letters")
     
    if len(keyword) == 0:
                     raise ValueError("Text cannot be empty")
             
    for letter in keyword:
             if letter.isalpha() == False or letter.isupper() == False:
                 raise ValueError("Text must contain only uppercase letters")

def encrypt_vigenere(plaintext, keyword):
    """Encrypt plaintext using a Vigenere cipher with a keyword.

    Add more implementation details here.
    """

    vigenere_error(plaintext, keyword)
        
    ciphertext = ""
    keyword = keyword_repeat(keyword, len(plaintext))
    for i in range(len(plaintext)):
        index = abc.index(plaintext[i]) + abc.index(keyword[i])
        if index > 25:
            index %= 26
        ciphertext += abc[index]

    return ciphertext



def decrypt_vigenere(ciphertext, keyword):
    """Decrypt ciphertext using a Vigenere cipher with a keyword.

    Add more implementation details here.
    """

    vigenere_error(ciphertext, keyword)

    plaintext = ""
    keyword = keyword_repeat(keyword, len(ciphertext))
    for i in range(len(ciphertext)):
        index = abc.index(ciphertext[i]) - abc.index(keyword[i])
        if index < 0:
            index = 26 + index

        plaintext += abc[index]
    
    return plaintext



#Scytale Cipher
def encrypt_scytale(plaintext, circumference):

    last_two_error(plaintext,circumference)

    ciphertext = ""
    for i in range(circumference):
        for j in range(0,len(plaintext),circumference):
            if (i+j) < len(plaintext):
                ciphertext += plaintext[j+i]
    return ciphertext


def last_two_error(text, number):
    if len(text) == 0:
                      raise ValueError("Text cannot be empty")
              
    for letter in text:
                  if letter.isalpha() == False or letter.isupper() == False:
                          raise ValueError("Text must contain only uppercase letters")

    if isinstance(number, int) == False or number <= 0:
         raise ValueError("The second parameter has to be a number, greater than 0")

def decrypt_scytale(ciphertext, circumference):

    last_two_error(ciphertext,circumference)
    plaintext = ""
    column = len(ciphertext) // circumference
    remainder = len(ciphertext) % circumference

    if remainder != 0:
        column += 1
    else:
        for i in range(column):
            for j in range(0, len(ciphertext), column):
                if (i+j)<len(ciphertext):
                    plaintext += ciphertext[i+j]
        return plaintext
    
    for i in range(column):
        j = -column
        count = 0
        while count < remainder + 1 and len(ciphertext)!=len(plaintext):
            j += column
            count += 1
            plaintext += ciphertext[i+j]

        j += column -1
        while( i+j < len(ciphertext) and len(ciphertext)!= len(plaintext)):
            plaintext += ciphertext[i+j]
            j += column -1


    return plaintext


def encrypt_railfence(plaintext, rail):

    last_two_error(plaintext,rail)

    if rail == 1:
        return plaintext

    ciphertext = ""

    index = []
    while len(index) < len(plaintext):
        for i in range(0, rail-1):
            index.append(i)
            if len(index) == len(plaintext):
                break
        if len(index) == len(plaintext):
                                    break
        for i in range(rail-1, 0, -1):
            index.append(i)
            if len(index) == len(plaintext):
                            break

    for i in range(rail):
        for j in range(0, len(index)):
            if index[j] == i:
                ciphertext += plaintext[j]
    return ciphertext

def decrypt_railfence(ciphertext, rail):
    last_two_error(ciphertext,rail)
    if rail == 1:
        return ciphertext

    index = []

    while len(index) < len(ciphertext):
        for i in range(0, rail-1):
            index.append(i)
            if len(index) == len(ciphertext):
                break
        if len(index) == len(ciphertext):
                break
        for i in range(rail-1, 0, -1):
            index.append(i)
            if len(index) == len(ciphertext):
                break

    plaintext = ""

    parts = []
    for i in range(rail):
        for j in range(index.count(i)):
            parts.append(i)

    

    for i in range(len(index)):
        j = 0
        while j < len(parts) and parts[j]!= index[i]:
             j +=1
        if j < len(parts):
             plaintext += ciphertext[j]
             parts[j] = -1
    return plaintext


def encrypt_railfence_bin(plaintext, rail):


    if rail == 1:
        return plaintext

    ciphertext = []

    index = []
    while len(index) < len(plaintext):
        for i in range(0, rail-1):
            index.append(i)
            if len(index) == len(plaintext):
                break
        if len(index) == len(plaintext):
                                    break
        for i in range(rail-1, 0, -1):
            index.append(i)
            if len(index) == len(plaintext):
                            break

    for i in range(rail):
        for j in range(0, len(index)):
            if index[j] == i:
                ciphertext.append(plaintext[j])
    return bytes(ciphertext)

def decrypt_railfence_bin(ciphertext, rail):

    if rail == 1:
        return ciphertext

    index = []

    while len(index) < len(ciphertext):
        for i in range(0, rail-1):
            index.append(i)
            if len(index) == len(ciphertext):
                break
        if len(index) == len(ciphertext):
                break
        for i in range(rail-1, 0, -1):
            index.append(i)
            if len(index) == len(ciphertext):
                break

    plaintext = []

    parts = []
    for i in range(rail):
        for j in range(index.count(i)):
            parts.append(i)

    

    plaintext = bytearray()
    used = [False] * len(ciphertext)

    for r in index:
        for j in range(len(parts)):
            if not used[j] and parts[j] == r:
                plaintext.append(ciphertext[j])
                used[j] = True
                break

    return bytes(plaintext)
   


# Merkle-Hellman Knapsack Cryptosystem

def generate_private_key(n=8):
    """Generate a private key for use in the Merkle-Hellman Knapsack Cryptosystem.

    Following the instructions in the handout, construct the private key components
    of the MH Cryptosystem. This consistutes 3 tasks:

    1. Build a superincreasing sequence `w` of length n
        (Note: you can check if a sequence is superincreasing with `utils.is_superincreasing(seq)`)
    2. Choose some integer `q` greater than the sum of all elements in `w`
    3. Discover an integer `r` between 2 and q that is coprime to `q` (you can use utils.coprime)

    You'll need to use the random module for this function, which has been imported already

    Somehow, you'll have to return all of these values out of this function! Can we do that in Python?!

    @param n bitsize of message to send (default 8)
    @type n int

    @return 3-tuple `(w, q, r)`, with `w` a n-tuple, and q and r ints.
    """
    raise NotImplementedError  # Your implementation here

def create_public_key(private_key):
    """Create a public key corresponding to the given private key.

    To accomplish this, you only need to build and return `beta` as described in the handout.

        beta = (b_1, b_2, ..., b_n) where b_i = r × w_i mod q

    Hint: this can be written in one line using a list comprehension

    @param private_key The private key
    @type private_key 3-tuple `(w, q, r)`, with `w` a n-tuple, and q and r ints.

    @return n-tuple public key
    """
    raise NotImplementedError  # Your implementation here


def encrypt_mh(message, public_key):
    """Encrypt an outgoing message using a public key.

    1. Separate the message into chunks the size of the public key (in our case, fixed at 8)
    2. For each byte, determine the 8 bits (the `a_i`s) using `utils.byte_to_bits`
    3. Encrypt the 8 message bits by computing
         c = sum of a_i * b_i for i = 1 to n
    4. Return a list of the encrypted ciphertexts for each chunk in the message

    Hint: think about using `zip` at some point

    @param message The message to be encrypted
    @type message bytes
    @param public_key The public key of the desired recipient
    @type public_key n-tuple of ints

    @return list of ints representing encrypted bytes
    """
    raise NotImplementedError  # Your implementation here

def decrypt_mh(message, private_key):
    """Decrypt an incoming message using a private key

    1. Extract w, q, and r from the private key
    2. Compute s, the modular inverse of r mod q, using the
        Extended Euclidean algorithm (implemented at `utils.modinv(r, q)`)
    3. For each byte-sized chunk, compute
         c' = cs (mod q)
    4. Solve the superincreasing subset sum using c' and w to recover the original byte
    5. Reconsitite the encrypted bytes to get the original message back

    @param message Encrypted message chunks
    @type message list of ints
    @param private_key The private key of the recipient
    @type private_key 3-tuple of w, q, and r

    @return bytearray or str of decrypted characters
    """
    raise NotImplementedError  # Your implementation here

