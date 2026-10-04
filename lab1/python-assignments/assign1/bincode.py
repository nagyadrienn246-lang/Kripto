import random

from crypto import (encrypt_railfence_bin, decrypt_railfence_bin)

def main():
    with open("image.jpg", "rb") as f:
        original = f.read()

    encrypted = encrypt_railfence_bin(original, 3)

    with open("encrypted.bin", "wb") as f:
        f.write(encrypted)

    decrypted = decrypt_railfence_bin(encrypted, 3)

    with open("decrypted.jpg", "wb") as f:
        f.write(decrypted)

    print(original == decrypted)


if __name__ == '__main__':
    main()