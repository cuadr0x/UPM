* Inicializa el SP y el PC
**************************
		ORG     $0
        DC.L    $8000			* Pila
        DC.L    PPAL        	* PC

        ORG     $400

* Definición de equivalencias
*********************************

MR1A    EQU     $effc01       * de modo A (escritura)
MR2A    EQU     $effc01       * de modo A (2º escritura)
SRA     EQU     $effc03       * de estado A (lectura)
CSRA    EQU     $effc03       * de seleccion de reloj A (escritura)
CRA     EQU     $effc05       * de control A (escritura)
TBA     EQU     $effc07       * buffer transmision A (escritura)
RBA     EQU     $effc07       * buffer recepcion A  (lectura)
ACR		EQU		$effc09	      * de control auxiliar
IMR     EQU     $effc0B       * de mascara de interrupcion A (escritura)
ISR     EQU     $effc0B       * de estado de interrupcion A (lectura)
MR1B    EQU     $effc11       * de modo B (escritura)
MR2B    EQU     $effc11       * de modo B (2º escritura)
CRB     EQU     $effc15	      * de control A (escritura)
TBB     EQU     $effc17       * buffer transmision B (escritura)
RBB		EQU		$effc17       * buffer recepcion B (lectura)
SRB     EQU     $effc13       * de estado B (lectura)
CSRB	EQU		$effc13       * de seleccion de reloj B (escritura)
IVR		EQU		$effc19	      * vector de interrupcion



**************************** INIT ************************************************



INIT:   
		MOVE.B          #%00010000,CRA      * Reinicia el puntero MR1 en la linea A
        MOVE.B          #%00000011,MR1A     * 8 bits por caracter linea A
        MOVE.B          #%00000000,MR2A     * Eco desactivado en la linea A
        MOVE.B          #%11001100,CSRA     * Velocidad = 38400 bps
        MOVE.B          #%00000000,ACR      * Velocidad = 38400 bps
        MOVE.B          #%00000101,CRA      * Transmision y recepcion activados.

		MOVE.B          #%00010000,CRB     	* Reinicia el puntero MR1 en la linea B
        MOVE.B          #%00000011,MR1B    	* 8 bits por caracter linea B
        MOVE.B          #%00000000,MR2B   	* Eco desactivado en la linea B
        MOVE.B          #%11001100,CSRB   	* Velocidad = 38400 bps
        MOVE.B          #%00000101,CRB   	* Transmision y recepcion activados

		MOVE.B 			#$40,IVR	  		* Vector de interrupcion 40

		MOVE.B			#%00100010,IMR	  	* Habilita interrupciones de recepcion en A y B
		MOVE.B  		#%00100010,IMRCopia
	
		MOVE.L 			#RTI,$100 	 		* Actualiza la rutina de tratatamiento de interrupcion en la tabla de valores de interrupcion

		* Inicializacion de buffers, contadores y punteros

		MOVE.L      	#BScanA,D1	* Guardamos la dirección del Buffer en D1
		MOVE.L 			D1,GetSA	* Inicializamos los atributos del Buffer con sus valores, los tres primeros se inicializan al valor de BScanA que es la 
		MOVE.L 			D1,PutSA	* primera dirección del buffer, en cambio la última, se inicializa al final que sería la dir inicial (BScanA) + su tamaño (2001)
		ADD.L 			#2001,D1	* Esto lo hacemos para los demás Buffer también
		MOVE.L			D1,FinSA

		MOVE.L 			#BScanB,D1 
		MOVE.L 			D1,GetSB
		MOVE.L 			D1,PutSB
		ADD.L 			#2001,D1
		MOVE.L			D1,FinSB

		MOVE.L 			#BPrintA,D1
		MOVE.L 			D1,GetPA
		MOVE.L 			D1,PutPA
		ADD.L 			#2001,D1
		MOVE.L			D1,FinPA

		MOVE.L 			#BPrintB,D1
		MOVE.L 			D1,GetPB
		MOVE.L 			D1,PutPB
		ADD.L 			#2001,D1
		MOVE.L			D1,FinPB
		
		RTS
	


***********************LEECAR*********************************


LEECAR:		
		CMP.L		#0,D0			* Realizamos las comparaciones con el descriptor para saber de que buffer queremos leer
		BEQ		LEECARSA
		CMP.L		#1,D0	
		BEQ		LEECARSB
		CMP.L		#2,D0		
		BEQ		LEECARPA
		CMP.L		#3,D0		
		BEQ		LEECARPB
LEECARSA:	
		MOVE.L 	#GetSA,A0			* Guardamos las direcciones de las etiquetas que vamos a utilizar
		MOVE.L 	#PutSA,A2
		MOVE.L 	#FinSA,A3
		MOVE.L  #BScanA,A4	
		MOVE.L 	(A0),A1	 			* Guardamos los valores necesarios de las etiquetas en registros
		MOVE.L 	(A2),A2				* A1 = Puntero extracción    A2 = Puntero de inserción	  A4 = InicioBuffer    A3 = FinBuffer
		MOVE.L	(A3),A3							
		JMP		GENERAL		 	
LEECARSB:	
		MOVE.L 	#GetSB,A0
		MOVE.L 	#PutSB,A2
		MOVE.L 	#FinSB,A3
		MOVE.L  #BScanB,A4	
		MOVE.L 	(A0),A1	 
		MOVE.L 	(A2),A2
		MOVE.L	(A3),A3
		JMP		GENERAL
LEECARPA:	
		MOVE.L 	#GetPA,A0
		MOVE.L 	#PutPA,A2
		MOVE.L 	#FinPA,A3
		MOVE.L  #BPrintA,A4	
		MOVE.L 	(A0),A1	 
		MOVE.L 	(A2),A2
		MOVE.L	(A3),A3
		JMP		GENERAL
LEECARPB:	
		MOVE.L 	#GetPB,A0	
		MOVE.L 	#PutPB,A2
		MOVE.L 	#FinPB,A3
		MOVE.L  #BPrintB,A4	
		MOVE.L 	(A0),A1		
		MOVE.L 	(A2),A2
		MOVE.L	(A3),A3	
GENERAL:		
		CMP.L 	A1,A2				* Comparamos para ver si está vacio el buffer para devolver -1 en ese caso		
		BEQ		EMPTY		
		MOVE.L	#0,D0				* Reseteamos D0
		MOVE.B 	(A1)+,D0			* Guardamos el caracter del buffer en D0 y lo postincrementamos el puntero de extracción
		CMP.L 	A3,A1				* Comparamos para ver si estamos al final del buffer, si es el caso, saltamos a FINAL para 
		BEQ		FINAL       		* cambiar la dirección del puntero de extracción al principio del buffer
		MOVE.L 	A1,(A0)				* Actualizamos el valor de la etiqueta del puntero de extracción con su valor incrementado
		JMP		LEECARF
FINAL:	
		MOVE.L 	A4,A1				* Ponemos el valor del inicio del buffer en el puntero de extracción
		MOVE.L 	A1,(A0)				* Actualizamos el valor de la etiqueta del puntero de extracción con su nuevo valor
		JMP		LEECARF
EMPTY:		
		MOVE.L 	#$FFFFFFFF,D0		* Devolvemos -1 en caso de que esté vacío
LEECARF:
		RTS


***********************ESCCAR*********************************

			
ESCCAR:		
		CMP.L		#0,D0			* Realizamos las comparaciones con el descriptor para saber en que buffer queremos escribir	
		BEQ		ESCCARSA
		CMP.L		#1,D0		
		BEQ		ESCCARSB
		CMP.L		#2,D0		
		BEQ		ESCCARPA
		CMP.L		#3,D0		
		BEQ		ESCCARPB
ESCCARSA:	
		MOVE.L 	#GetSA,A0			* Guardamos las direcciones de las etiquetas que vamos a utilizar
		MOVE.L 	#PutSA,A2
		MOVE.L 	#FinSA,A4
		MOVE.L  #BScanA,A5	
		MOVE.L 	(A0),A1	 			* Guardamos los valores necesarios de las etiquetas en registros
		MOVE.L 	(A2),A3				* A1 = Puntero extracción   A3 = Puntero de inserción    A4 = FinBuffer    A5 = InicioBuffer	   
		MOVE.L	(A4),A4
		JMP 	GENERAL2			
ESCCARSB:	
		MOVE.L 	#GetSB,A0
		MOVE.L 	#PutSB,A2
		MOVE.L 	#FinSB,A4
		MOVE.L  #BScanB,A5	
		MOVE.L 	(A0),A1	 
		MOVE.L 	(A2),A3
		MOVE.L	(A4),A4
		JMP		GENERAL2
ESCCARPA:	
		MOVE.L 	#GetPA,A0
		MOVE.L 	#PutPA,A2
		MOVE.L 	#FinPA,A4
		MOVE.L  #BPrintA,A5
		MOVE.L 	(A0),A1	 
		MOVE.L 	(A2),A3
		MOVE.L	(A4),A4
		JMP		GENERAL2
ESCCARPB:	
		MOVE.L 	#GetPB,A0	
		MOVE.L 	#PutPB,A2
		MOVE.L 	#FinPB,A4
		MOVE.L  #BPrintB,A5
		MOVE.L 	(A0),A1		
		MOVE.L 	(A2),A3
		MOVE.L	(A4),A4
GENERAL2:		
		MOVE.B	D1,(A3)+			* Guardamos el valor del caracter en el buffer y postincrementamos su puntero de inserción
		CMP.L	A4,A3				* Comparamos para ver si hemos llegado al final del buffer con el puntero de inserción, si es 
		BEQ		FINAL2				* el caso, saltamos a FINAL2 para cambiar la dirección del puntero de inserción al principio del buffer
GENERAL3:		
		CMP.L	A1,A3				* Comparamos para ver si el buffer está lleno tras la escritura en él, si lo está salta a FULL
		BEQ		FULL		
		MOVE.L  A3,(A2)				* Actualizamos el valor de la etiqueta del puntero de extracción con su valor incrementado
		MOVE.L 	#0,D0				* Devolvemos un 0 de D0 para simbolizar que se ha terminado con éxito la escritura
		JMP		ESCFIN
FINAL2:	
		MOVE.L 	A5,A3				* Ponemos el valor del inicio del buffer en el puntero de inserción
		JMP		GENERAL3			* Llamamos a GENERAL3 para que se actualice el valor en la etiqueta y que revise si está lleno tras el cambio
FULL:
		MOVE.L 	#$FFFFFFFF,D0		* Devolvemos un -1 en D0 para simbolizar que el buffer está lleno
ESCFIN:					
		RTS



************************PRINT******************



PRINT:
		LINK 		A6,#0			* Creamos el marco de pila para referenciar los parametros de entrada con facilidad
		MOVE.L		#0,D1 			* reseteamos D1 y D2
		MOVE.L		#0,D2	
		MOVE.L		#0,D3			* Inicializamos el contador a 0
		MOVE.L		8(A6),A1		* A1 = Buffer
		MOVE.W		12(A6),D1		* D1 = Descriptor
		MOVE.W		14(A6),D2		* D2 = Tamaño
		CMP.W 		#0,D2			* Comprobamos que el tamaño del buffer es distinto de 0, si no lo es llamamos a CAR0 para que haga return de un 0 en D0
		BEQ 		CAR0
		CMP.W		#0,D1   		* Comparamos para ver si el descriptor es de la línea A o B
		BEQ			PA
		CMP.W		#1,D1
		BEQ			PB
		MOVE.L		#$FFFFFFFF,D0   * En caso que no sea de ninguna línea devolvemos -1 para indicar que se ha producido un error
		JMP			FINPRINT
PA:
		CMP.W		#0,D2			* Si no nos quedan más caracteres por meter en el buffer, saltamos a OKPA
		BEQ			OKPA
		MOVE.B		(A1)+,D1
		MOVE.L		D3,-(A7)		* PUSH contador
		MOVE.W 		D2,-(A7)		* PUSH tamaño
		MOVE.L		A1,-(A7)		* PUSH buffer
		MOVE.L		#2,D0       	* Seleccionamos el BufferPA
		BSR			ESCCAR
		MOVE.L 		(A7)+,A1		* POP buffer
		MOVE.W 		(A7)+,D2		* POP tamaño
		MOVE.L		(A7)+,D3		* POP contador
		CMP.L		#$FFFFFFFF,D0   * Si el ESCCAR nos devuelve -1, es decir que su buffer está lleno, paramos de copiar y llamamos a OKPA
		BEQ			OKPA
		ADD.L		#1,D3			* Incrementamos el contador, y decrementamos el tamaño
		SUB.W		#1,D2
		JMP			PA
PB:
		CMP.W		#0,D2			* Si no nos quedan más caracteres por meter en el buffer, saltamos a OKPB
		BEQ			OKPB
		MOVE.B		(A1)+,D1
		MOVE.L		D3,-(A7)		* PUSH contador
		MOVE.W 		D2,-(A7)		* PUSH tamaño
		MOVE.L		A1,-(A7)		* PUSH buffer
		MOVE.L		#3,D0       	* Seleccionamos el BufferPB
		BSR			ESCCAR
		MOVE.L 		(A7)+,A1		* POP buffer
		MOVE.W 		(A7)+,D2		* POP tamaño
		MOVE.L		(A7)+,D3		* POP contador
		CMP.L		#$FFFFFFFF,D0   * Si el ESCCAR nos devuelve -1, es decir que su buffer está lleno, paramos de copiar y llamamos a OKPB
		BEQ			OKPB
		ADD.L		#1,D3			* Incrementamos el contador, y decrementamos el tamaño
		SUB.W		#1,D2
		JMP			PB
OKPA:
		CMP.L		#0,D3			* Comprobamos que se ha escritos algún caracter, es decir el contador != 0, si no es así, salta a CAR0 para hacer return de un 0
		BEQ			CAR0
		MOVE.W		SR,D4			* Guardamos el vector de interrupcion en D4 para cambiarlo momentaneamente sin alterarlo
		MOVE.W		#$2700,SR   	* Prohibimos las interrupciones momentaneamente
		BSET		#0,IMRCopia		* Ponemos el bit 0 a 1 para que se produzcan interrupciones e imprima en pantalla en la línea A
		MOVE.B		IMRCopia,IMR 	* Guardamos el valor de la copia en el de IMR real
		MOVE.W		D4,SR       	* Volvemos a restaurar el vector de interrupcion
		MOVE.L 		D3,D0       	* Hacemos return con el número de caracteres escritos en el buffer en D0
		JMP 		FINPRINT	
OKPB:
		CMP.L		#0,D3  			* Comprobamos que se ha escritos algún caracter, es decir el contador != 0, si no es así, salta a CAR0 para hacer return de un 0
		BEQ			CAR0
		MOVE.W		SR,D4			* Guardamos el vector de interrupcion en D4 para cambiarlo momentaneamente sin alterarlo
		MOVE.W		#$2700,SR   	* Prohibimos las interrupciones momentaneamente
		BSET		#4,IMRCopia 	* Ponemos el bit 0 a 1 para que se produzcan interrupciones e imprima en pantalla en la línea B
		MOVE.B		IMRCopia,IMR 	* Guardamos el valor de la copia en el de IMR real
		MOVE.W		D4,SR     		* Volvemos a restaurar el vector de interrupcion
		MOVE.L 		D3,D0       	* Hacemos return con el número de caracteres escritos en el buffer en D0
		JMP 		FINPRINT
CAR0:
		MOVE.L 		#0,D0       	* Return 0 en D0
		JMP 		FINPRINT
FINPRINT:
		UNLK		A6
		RTS


***********************SCAN*******************



SCAN:
		LINK		A6,#0			* Crea el marco de pila
		MOVE.L		8(A6),A0		* A0 = Direccion del buffer
		MOVE.L 		#0,D1
		MOVE.L		#0,D2
		MOVE.W		12(A6),D1		* D1 = Descriptor
		MOVE.W		14(A6),D2		* D2 = Tamaño
		MOVE.L		#0,D3			* Contador
		MOVE.L		#0,D0			* Resetea D0
		CMP.W		#0,D2			* Si el tamaño es 0 salta a FINSCAN
		BLE			FINSCAN
		CMP.W 		#1,D1			* Compara si el descriptor es ScanB
		BEQ			SB
		CMP.W		#0,D1			* Compara si el descriptor es ScanA
		BEQ			SA
		JMP			ERRORS
SA:
		MOVE.L		D3,-(A7)		* PUSH contador
		MOVE.W 		D2,-(A7)		* PUSH tamaño
		MOVE.L		A0,-(A7)		* PUSH buffer
		MOVE.L		#0,D0			* Elige el buffer BScanA
		BSR 		LEECAR
		MOVE.L 		(A7)+,A0		* POP buffer
		MOVE.W 		(A7)+,D2		* POP tamaño
		MOVE.L		(A7)+,D3		* POP contador
		CMP.L		#$FFFFFFFF,D0	* Si hay algun error en los parametros salta a fin
		BEQ			OKS
		ADD.L		#1,D3			* Incrementa el contador
		MOVE.B		D0,(A0)+		* Guarda el caracter devuelto por LEECAR en el buffer
		SUB.W		#1,D2			* Decrementa el tamaño
		CMP.W		#0,D2			* Compara si el tamaño es 0 y salta a OKS
		BEQ			OKS
		JMP			SA				* Si no, salta al bucle SA
SB:
		MOVE.L		D3,-(A7)		* PUSH contador
		MOVE.W 		D2,-(A7)		* PUSH tamaño
		MOVE.L		A0,-(A7)		* PUSH buffer
		MOVE.L		#1,D0			* Elige el buffer BScanB
		BSR 		LEECAR
		MOVE.L 		(A7)+,A0		* POP buffer
		MOVE.W 		(A7)+,D2		* POP tamaño
		MOVE.L		(A7)+,D3		* POP contador
		CMP.L		#$FFFFFFFF,D0	* Si hay algun error en los parametros salta a fin
		BEQ			OKS
		ADD.L		#1,D3			* Incrementa el contador
		MOVE.B		D0,(A0)+		* Guarda el caracter devuelto por LEECAR en el buffer
		SUB.W		#1,D2			* Decrementa el tamaño
		CMP.W		#0,D2			* Compara si el tamaño es igual a 0 y salta a OKS
		BEQ			OKS
		JMP			SB				* Si no, vuelve al bucle SB
ERRORS:
		MOVE.L		#$FFFFFFFF,D0	* Fallo por descriptor invalido
		JMP 		FINSCAN
OKS:	
		MOVE.L		D3,D0			* Devuelve el numero de caracteres leidos
FINSCAN:
		UNLK		A6
		RTS


***********************RTI********************

RTI:
		MOVE.L 		D0,-(A7)		* Hacemos push de todos los registros que utlizamos en el programa para luego restaurarlos
		MOVE.L		D1,-(A7)
		MOVE.L 		D2,-(A7)
		MOVE.L 		D3,-(A7)
		MOVE.L 		D4,-(A7)
		MOVE.L 		A0,-(A7)
		MOVE.L 		A1,-(A7)
		MOVE.L 		A2,-(A7)
		MOVE.L 		A3,-(A7)
		MOVE.L 		A4,-(A7)
BUCLE1:
		MOVE.B 	ISR,D0				* Copiamos el registro de estado de interrupción en D0
		AND.B	IMRCopia,D0			* Realizamos un AND entre IMR y el registro de estado de interrupción para saber que líneas solicitan interrupción
		BTST	#1,D0				* Identificamos la línea que solicita la interrupción
		BNE		RECEPA
		BTST 	#5,D0	
		BNE		RECEPB
		BTST	#0,D0	
		BNE		TRANSA
		BTST	#4,D0	
		BNE		TRANSB
		JMP		FINRTI		
RECEPA:	
		MOVE.B 	RBA,D1				* Copiamos el valor del registro de datos en D1
		MOVE.L 	#0,D0				* Identificamos el buffer para llamar a ESCCAR
		BSR 	ESCCAR
		CMP.L 	#$FFFFFFFF,D0		* Comparamos el valor devuelto por ESCCAR con -1 para ver si el buffer está lleno
		BEQ		FINRTI				* Si está lleno saltamos a FINRTI para por que ya no se puede escribir más
		JMP		BUCLE1			
RECEPB:		
		MOVE.B 	RBB,D1			
		MOVE.L 	#1,D0
		BSR 	ESCCAR
		CMP.L 	#$FFFFFFFF,D0
		BEQ		FINRTI	
		JMP		BUCLE1			
TRANSA:		
		MOVE.L 	#2,D0				* Identificamos el buffer para llamar a LEECAR
		BSR		LEECAR
		CMP.L 	#$FFFFFFFF,D0		* Comprobamos si el LEECAR ha devuelto -1, si es el caso, llamamos a DESHABA para que se deshabiliten las interrupciones
		BEQ		DESHABA
		MOVE.B 	D0,TBA              * Escribimos en el registro de datos el valor devuelto por LEECAR
		JMP 	BUCLE1				
TRANSB:		
		MOVE.L 	#3,D0
		BSR		LEECAR
		CMP.L 	#$FFFFFFFF,D0
		BEQ		DESHABB
		MOVE.B 	D0,TBB
		JMP 	BUCLE1
DESHABA:	
		BCLR	#0,IMRCopia			* Ponemos a 0 el bit en la posición 0 de IMR para que no se produzcan más interrupciones en esta línea
		MOVE.B 	IMRCopia,IMR    	* Actualizamos el valor de IMR
		JMP		BUCLE1

DESHABB:	
		BCLR	#4,IMRCopia         * Ponemos a 0 el bit en la posición 4 de IMR para que no se produzcan más interrupciones en esta línea
		MOVE.B 	IMRCopia,IMR        * Actualizamos el valor de IMR
		JMP		BUCLE1			
FINRTI:		
		MOVE.L		(A7)+,A4		* Restauramos los valores de los registros
		MOVE.L		(A7)+,A3
		MOVE.L		(A7)+,A2
		MOVE.L		(A7)+,A1
		MOVE.L		(A7)+,A0
		MOVE.L		(A7)+,D4
		MOVE.L		(A7)+,D3
		MOVE.L		(A7)+,D2
		MOVE.L		(A7)+,D1
		MOVE.L		(A7)+,D0
		RTE

******************************************************************

BScanA:		DS.B 	2001			* Recepción línea A
BScanB:		DS.B	2001			* Recepción línea B 
BPrintA:	DS.B 	2001			* Transmisión línea A
BPrintB:	DS.B 	2001			* Transmisión línea B	

GetSA:		DC.L 	BScanA			* Puntero de extracción del buffer BScanA
PutSA:		DC.L 	BScanA			* Puntero de inserción del buffer BScanA
FinSA:		DC.L 	BScanA+2001		* Posición final del buffer BScanA

GetSB:		DC.L 	BScanA			* Puntero de extracción del buffer BScanB
PutSB:		DC.L 	BScanA			* Puntero de inserción del buffer BScanB
FinSB:		DC.L 	BScanA+2001		* Posición final del buffer BScanB

GetPA:		DC.L 	BScanA			* Puntero de extracción del buffer BPrintA
PutPA:		DC.L 	BScanA			* Puntero de inserción del buffer BPrintA
FinPA:		DC.L 	BScanA+2001		* Posición final del buffer BPrintA

GetPB:		DC.L 	BScanA			* Puntero de extracción del buffer BPrintB
PutPB:		DC.L 	BScanA			* Puntero de inserción del buffer BPrintB
FinPB:		DC.L 	BScanA+2001		* Posición final del buffer BPrintB

IMRCopia:	DS.B 	1


	
**************PROGRAMA PRINCIPAL***********************************
	NOP
PPAL:
	MOVE.L 		#BUS_ERROR,8 	* Bus error handler
	MOVE.L 		#ADDRESS_ER,12 	* Address error handler
	MOVE.L 		#ILLEGAL_IN,16 	* Illegal instruction handler
	MOVE.L 		#PRIV_VIOLT,32 	* Privilege violation handler
	MOVE.L 		#ILLEGAL_IN,40 	* Illegal instruction handler
	MOVE.L 		#ILLEGAL_IN,44 	* Illegal instruction handler
	BSR 		INIT
	MOVE.W		#$2000,SR
BUCLEPP:
	MOVE.L		#20,D2		* tamaño del bloque
	MOVE.L		#BUFFER,A1	* direccion de buffer
	MOVE.L		#0,D1		* seleccion de linea de transmision A
BUCLESC:
	MOVE.L		D3,-(A7)	* push contador
	MOVE.W 		D2,-(A7)	* push tamaño
	MOVE.W 		D1,-(A7)	* push descriptor
	MOVE.L 		A1,-(A7)	* push buffer
	BSR		SCAN
	MOVE.L		(A7)+,A1	* pop buffer
	MOVE.W		(A7)+,D1	* pop descriptor
	MOVE.W		(A7)+,D2	* pop tamaño
	MOVE.L		(A7)+,D3	* pop contador
	ADD.L		D0,A1		* incrementa el puntero a memoria del buffer con el numero de caracteres escaneados
	SUB.W		D0,D2		* resta los caracteres ya escaneados al tamaño
	BLE		PRINTPP		
	JMP		BUCLESC
PRINTPP:
	MOVE.L		#20,D2		* tamaño del bloque
	MOVE.L		#BUFFER,A1	* direccion de buffer
	MOVE.L		#1,D1		* seleccion de linea de transmision B
	MOVE.W 		D2,-(A7)	* push tamaño
	MOVE.W 		D1,-(A7)	* push descriptor
	MOVE.L 		A1,-(A7)	* push buffer
	BSR		PRINT
	JMP		BUCLEPP
	
BUS_ERROR: BREAK * Bus error handler
	   NOP
	
ADDRESS_ER: BREAK * Address error handler
            NOP
	
ILLEGAL_IN: BREAK * Illegal instruction handler
	    NOP
	
PRIV_VIOLT: BREAK * Privilege violation handler
	    NOP
	
	
BUFFER:	DS.B 		2000	

*------

*************
