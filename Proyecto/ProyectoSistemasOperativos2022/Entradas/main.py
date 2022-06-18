import random

lineasEscrbir = []

def generarPedidos():
    #Lista comercios
    stringComercios = "Lonesome Dove,Bobbys,Melting Pot,Daytime Place,Easy Eats,Macro Bites,Grubber Hub,Cheerful Rhino,Home Cooking Experience,Fare & Feed,Golden Palace,Soups & Snacks,Quick Bite,Fast & Friendly,Big Bites,Blind Pig,Eatable,Eatery,Goodies,Lard Boy,Many Foods,Me Likey,Wonton Express,Great Burger"

    #Largo 30
    comercios = stringComercios.split(",")
    #Largo 3
    tipos = ["almacen", "farmacia", "restaurante"]
    #Solo para saber que poner
    Info = "ID,nombrecomercio,Tipo de Comercio,tiempoelab,distancia,horaingreso"
    farmacias = ["Farmacity","Farmashop","San roque"]
    comercios = ["Disco","TaTa","Kinko"]
    restaurantes = stringComercios.split(",")

    aux = 0

    for i in range(1000):
        
        #Aca consigo los randoms
        tipo = random.randint(0,9)
        randomRestaurante = random.randint(0,23)
        randomFarmacia = random.randint(0,2)
        randomComercio = random.randint(0,2)
        tiempoElab = random.randint(10,45)
        distancia = random.randint(5,20)
        horaIng = aux + random.randint(0,5)
        aux = horaIng

        #Aca me armo el tipo de pedido
        if(tipo > 2):
            tipo = 2
        if(tipo == 1 or tipo == 0):
            tiempoElab = 0
        
        if(tipo == 2):
            lineasEscrbir.append(f"{i},{restaurantes[randomRestaurante]},restaurante,{tiempoElab},{distancia},{horaIng}")
        elif tipo == 1:
            lineasEscrbir.append(f"{i},{farmacias[randomFarmacia]},farmacia,{tiempoElab},{distancia},{horaIng}")
        else:
            lineasEscrbir.append(f"{i},{comercios[randomComercio]},almacen,{tiempoElab},{distancia},{horaIng}")

    #Aca escribo
    file1 = open("Entradas/Pedidos.csv","w")
    for linea in lineasEscrbir[::-1]:
        file1.write(linea+"\n")
    file1.close()

generarPedidos()