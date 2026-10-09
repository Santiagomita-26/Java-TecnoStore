/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import dao.UsuarioDao;
import model.Usuario;


public class UsuarioController {
    
    private final UsuarioDao usuarioDao = new UsuarioDao();
    
    public Usuario login(String correo , String password){
        return usuarioDao.login(correo, password);
    }
}
