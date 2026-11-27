package cl.duoc.excursiones.dao;

import cl.duoc.excursiones.conexion.ConexionBD;
import cl.duoc.excursiones.model.Excursion;
import cl.duoc.excursiones.model.Kayak;
import cl.duoc.excursiones.model.Trekking;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ExcursionDAOMySQL implements ExcursionDAO {

    private static final String SELECT_ALL = "SELECT * FROM excursion";
    private static final String INSERT_TREKKING = "INSERT INTO excursion(id_tipo,codigo,participantes,distancia_km,desnivel_metros,guia_bilingue,seguro_incluido,duracion_horas) VALUES (1,?,?,?,?,?,?,?)";
    private static final String INSERT_KAYAK = "INSERT INTO excursion(id_tipo,codigo,participantes,distancia_km,desnivel_metros,guia_bilingue,seguro_incluido,duracion_horas) VALUES (2,?,?,?,?,?,?,?)";
    private static final String UPDATE_TREKKING = "UPDATE excursion SET id_tipo = 1, codigo = ?, participantes = ?, distancia_km = ?, desnivel_metros = ?, guia_bilingue = ?, seguro_incluido = ?, duracion_horas = ? WHERE id = ?";
    private static final String UPDATE_KAYAK = "UPDATE excursion SET id_tipo = 2, codigo = ?, participantes = ?, distancia_km = ?, desnivel_metros = ?, guia_bilingue = ?, seguro_incluido = ?, duracion_horas = ? WHERE id = ?";
    private static final String DELETE = "DELETE FROM excursion WHERE id = ?";


    @Override
    public List<Excursion> obtenerTodos() throws PersistenciaException{
        List<Excursion> todo = new ArrayList<>();

        try(
                Connection con = ConexionBD.getConnection();
                PreparedStatement ps = con.prepareStatement(SELECT_ALL);
                ResultSet rs = ps.executeQuery();
                )
        {
            while (rs.next()){
                todo.add(ExcursionMapper.mapear(rs));
            }

        }catch (SQLException | PersistenciaException e){
            throw new PersistenciaException("No se pudo obtener excursiones. ("+e.getMessage()+")",e);
        }

        return todo;
    }

    @Override
    public void insertar(Excursion excursion) throws PersistenciaException{
        String query = null;

        if (excursion instanceof Trekking){
            query = INSERT_TREKKING;
        } else if (excursion instanceof Kayak) {
            query = INSERT_KAYAK;
        }else{
            throw new PersistenciaException("Tipo de excursión inválido.");
        }

        try(
                Connection con = ConexionBD.getConnection();
                PreparedStatement ps = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

                )
        {
            ps.setString(1,excursion.getCodigo());
            ps.setInt(2,excursion.getParticipantes());
            ps.setDouble(3,excursion.getDistanciaKm());

            if(excursion instanceof Trekking){
                ps.setDouble(4,((Trekking) excursion).getDesnivelMetros());
                ps.setBoolean(5,((Trekking) excursion).isGuiaBilingue());
                ps.setBoolean(6,((Trekking) excursion).isSeguroIncluido());
                ps.setNull(7, Types.DOUBLE);

            } else if (excursion instanceof Kayak) {
                ps.setNull(4,Types.DOUBLE);
                ps.setNull(5,Types.BOOLEAN);
                ps.setNull(6,Types.BOOLEAN);
                ps.setDouble(7, ((Kayak) excursion).getDuracionHoras());
            }

            ps.executeUpdate();

            try(ResultSet idNuevo = ps.getGeneratedKeys()){
                if (idNuevo.next()){
                    excursion.setId(idNuevo.getInt(1));
                }
            }

        }catch (SQLException e){
            throw new PersistenciaException("No se pudo insertar. ("+e.getMessage()+")",e);
        }

    }

    @Override
    public void actualizar(Excursion excursion) throws PersistenciaException{
        String query = null;

        if (excursion instanceof Trekking){
            query = UPDATE_TREKKING;
        } else if (excursion instanceof Kayak) {
            query = UPDATE_KAYAK;
        }else{
            throw new PersistenciaException("Tipo de excursión inválido.");
        }

        try(
                Connection con = ConexionBD.getConnection();
                PreparedStatement ps = con.prepareStatement(query);

        )
        {
            ps.setString(1,excursion.getCodigo());
            ps.setInt(2,excursion.getParticipantes());
            ps.setDouble(3,excursion.getDistanciaKm());

            if(excursion instanceof Trekking){
                ps.setDouble(4,((Trekking) excursion).getDesnivelMetros());
                ps.setBoolean(5,((Trekking) excursion).isGuiaBilingue());
                ps.setBoolean(6,((Trekking) excursion).isSeguroIncluido());
                ps.setNull(7, Types.DOUBLE);

            } else if (excursion instanceof Kayak) {
                ps.setNull(4,Types.DOUBLE);
                ps.setNull(5,Types.BOOLEAN);
                ps.setNull(6,Types.BOOLEAN);
                ps.setDouble(7, ((Kayak) excursion).getDuracionHoras());
            }
            ps.setInt(8,excursion.getId());

            ps.executeUpdate();

        }catch (SQLException e){
            throw new PersistenciaException("No se pudo actualizar. ("+e.getMessage()+")",e);
        }

    }

    @Override
    public void eliminar(int id) throws PersistenciaException{

        try(
                Connection con = ConexionBD.getConnection();
                PreparedStatement ps = con.prepareStatement(DELETE);
                )
        {
            ps.setInt(1,id);
            ps.executeUpdate();

        }catch (SQLException e){
            throw new PersistenciaException("No se pudo eliminar. ("+e.getMessage()+")",e);
        }
    }

}
