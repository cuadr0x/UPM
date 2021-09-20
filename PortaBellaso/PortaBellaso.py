import re
import sys

# variables globales
password = ""
topAlphabet = []
bottomAlphabet = []
alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
text = ""
binaryText = ""
words = []
ciphwords = []
textOcurrences = {}
chunks = []
chunkAlphabet = []

# crea el los alfabetos utilizados en el programa
def createAlphabet():

    global topAlphabet,bottomAlphabet,alphabet
    count = 0
    for i in range(len(alphabet)):
        if(i < len(alphabet)/2):
            topAlphabet.append(alphabet[i])
        else:
            bottomAlphabet.append(alphabet[i])
                                                                                                                                                                                                                                           
def processInputWithWords(text_file, words_file):  

    global text,binaryText,words,ciphwords                                                                                                                                                                                                 
    # lectura del text a desencriptar                                                                                                                                                                                                      
    f = open(text_file, "r")                                                                                                                                                                                                             
    for line in f:                                                                                                                                                                                                                         
        text += line                                                                                                                                                                                                                       
    # tratamiento de la string                                                                                                                                                                                                             
    text = "".join(text.split())                                                                                                                                                                                                           
    # convertir el texto en binario en funcion de si esta en la fila de arriba o de abajo                                                                                                                                                  
    for char in text:                                                                                                                                                                                                                      
        if char in topAlphabet:                                                                                                                                                                                                            
            binaryText += "0"                                                                                                                                                                                                              
        else:                                                                                                                                                                                                                              
            binaryText += "1"                                                                                                                                                                                                              
    f.close()                                                                                                                                                                                                                              
    # lee las palabras clave que se encuentran en el texto                                                                                                                                                                                 
    f = open(words_file, "r")                                                                                                                                                                                                            
    words = []                                                                                                                                                                                                                             
    for i in f:                                                                                                                                                                                                                            
        words.append("".join(i.split()))                                                                                                                                                                                                   
    # convierte las palabras en binario pero al contratio que en el texto para posteriormente encontrar ocurrencias                                                                                                                        
    ciphwords = []                                                                                                                                                                                                                         
    for i in words:                                                                                                                                                                                                                        
        b = ""                                                                                                                                                                                                                             
        for x in i:                                                                                                                                                                                                                        
            if x in topAlphabet:                                                                                                                                                                                                           
                b += "1"                                                                                                                                                                                                                   
            else:                                                                                                                                                                                                                          
                b += "0"                                                                                                                                                                                                                   
        ciphwords.append(b)                                                                                                                                                                                                                
    f.close()  

def processInput(text_file):   

    global text,binaryText,words,ciphwords                                                                                                                                                                                                 
    # lectura del text a desencriptar                                                                                                                                                                                                      
    f = open(text_file, "r")                                                                                                                                                                                                             
    for line in f:                                                                                                                                                                                                                         
        text += line                                                                                                                                                                                                                       
    # tratamiento de la string                                                                                                                                                                                                             
    text = "".join(text.split())

def findOcurrences():  

    global textOcurrences,indexOcurrences
    indexOcurrences = {}                                                                                                                                                                                                                              
    for i in range(len(ciphwords)):                                                                                                                                                                                                                    
        indexOcurrences[words[i]] = [m.start() for m in re.finditer(ciphwords[i], binaryText)]                                                                                                                                                                                   
    textOcurrences = {}                                                                                                                                                                                                                          
    f = []                                                                                                                                                                                                                                 
    h = []  
    #AQUI
    for i in range(len(words)):
        f = []
        for j in indexOcurrences[words[i]]:
            f.append(text[j:j+len(words[i])])
        textOcurrences[words[i]] = f;

# define los diferentes alfabetos para cada par de caracteres de la contrasena
def getChunks():  

    global chunkAlphabet,chunks
    chunks = [alphabet[i:i+2] for i in range(0, len(alphabet), 2)]                                                                                                                                                                         
    chunkAlphabet = []                                                                                                                                                                                                                              
    for i in range(len(chunks)):
        auxList = []
        for x in range(len(topAlphabet)):
            b = ""
            if -i+x < 0:
                b += topAlphabet[x] + bottomAlphabet[len(bottomAlphabet)+x-i]
            elif -i+x >= 0:
                b += topAlphabet[x] + bottomAlphabet[-i+x]
            auxList.append(b)
        chunkAlphabet.append(auxList)

# encuentra las diferentes contrasenas a partir de las ocurrencias encontradas en el texto
def getPossiblePasswords():

    for i in range(len(words)):
        possiblePasswords = []
        for j in textOcurrences[words[i]]:
            clave = ""
            for letter in range(len(j)):
                tupla = ""
                if j[letter] in topAlphabet:
                    tupla = j[letter] + words[i][letter] 
                else:
                    tupla = words[i][letter] + j[letter]
                for k in range(len(chunkAlphabet)):
                    if tupla in chunkAlphabet[k]:
                        clave += chunks[k]+ "-"
            clave = clave[:-1]
            possiblePasswords.append(clave)
        print "Posibles contraseñas para ", words[i], ": ", " | ".join(possiblePasswords)

# descifra el texto en funcion de una clave que va rotando para encontrar el inicio correcto
def decipher(str):

    password = str.split("-")
    count = 0
    for k in range(len(password)):
        plainText = ""
        count = 0
        print "Texto descifrado con clave: ", str
        for i in text:
            for j in chunkAlphabet[chunks.index(password[count])]:
                if i in j:
                    j = j.replace(i,"")
                    plainText += j
            if(count+1 < len(password)):
                count+=1
            else:
                count = 0
        password.append(password.pop(0))
        print plainText,"\n\n\n"

def main():

    if len(sys.argv) != 4:
        print("Usage: python PortaBellaso.py claves [text file with words] [text file with the text]\nUsage: python PortaBellaso.py descifrar [clave] [text file with the text]")
        sys.exit(0)

    if sys.argv[1] == "claves":
        createAlphabet()
        processInputWithWords(sys.argv[2],sys.argv[3])
        findOcurrences()
        getChunks()
        getPossiblePasswords()

    if sys.argv[1] == "descifrar":
        createAlphabet()
        processInput(sys.argv[3])
        getChunks()
        decipher(sys.argv[2])
main()

