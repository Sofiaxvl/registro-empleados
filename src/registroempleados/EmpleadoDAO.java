package registroempleados;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class EmpleadoDAO {

    
    public void crear(Empleado e) throws SQLException {
        String sql = "INSERT INTO empleados (nombre, cargo, salario, correo) VALUES (?,?,?,?)";
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, e.getNombre());
            ps.setString(2, e.getCargo());
            ps.setDouble(3, e.getSalario());
            ps.setString(4, e.getCorreo());
            ps.executeUpdate();
        }
    }

  
    public List<Empleado> listar() throws SQLException {
        List<Empleado> lista = new ArrayList<>();
        String sql = "SELECT id, nombre, cargo, salario, correo FROM empleados ORDER BY id";
        try (Connection con = Conexion.getConexion();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(new Empleado(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("cargo"),
                        rs.getDouble("salario"),
                        rs.getString("correo")));
            }
        }
        return lista;
    }


    public void actualizar(Empleado e) throws SQLException {
        String sql = "UPDATE empleados SET nombre=?, cargo=?, salario=?, correo=? WHERE id=?";
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, e.getNombre());
            ps.setString(2, e.getCargo());
            ps.setDouble(3, e.getSalario());
            ps.setString(4, e.getCorreo());
            ps.setInt(5, e.getId());
            ps.executeUpdate();
        }
    }

    // DELETE: borra un empleado por id
    public void eliminar(int id) throws SQLException {
        String sql = "DELETE FROM empleados WHERE id=?";
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }
}