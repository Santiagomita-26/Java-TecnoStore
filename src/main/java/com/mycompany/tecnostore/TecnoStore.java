

package com.mycompany.tecnostore;

import dao.MarcaDao;
import model.Marca;




public class TecnoStore {

    public static void main(String[] args) {
      
              MarcaDao marcaDao = new MarcaDao();

        Marca marca = new Marca(2, "Samsung");

        marcaDao.eliminar(marca);

        marcaDao.listar();
    
    }
}


        