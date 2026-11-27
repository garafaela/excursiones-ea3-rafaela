package cl.duoc.excursiones.dao;

import cl.duoc.excursiones.model.Excursion;
import cl.duoc.excursiones.model.Kayak;
import cl.duoc.excursiones.model.Trekking;

import java.sql.ResultSet;
import java.sql.SQLException;

final class ExcursionMapper {

    static Excursion mapear(ResultSet resultadoQuery) throws SQLException, PersistenciaException{
        Excursion excursion = null;

        int id = resultadoQuery.getInt("id");
        int tipo = resultadoQuery.getInt("id_tipo");
        String codigo = resultadoQuery.getString("codigo");
        int participantes = resultadoQuery.getInt("participantes");
        double distanciaKm = resultadoQuery.getDouble("distancia_km");

        if (tipo == 1){
            // Trekking
            double desnivelMetros = resultadoQuery.getDouble("desnivel_metros");
            boolean guiaBilingue = resultadoQuery.getBoolean("guia_bilingue");
            boolean seguroIncluido = resultadoQuery.getBoolean("seguro_incluido");
            excursion = new Trekking(codigo,participantes,distanciaKm,desnivelMetros,guiaBilingue,seguroIncluido);

        } else if (tipo == 2) {
            // Kayak
            double duracionHoras = resultadoQuery.getDouble("duracion_horas");
            excursion = new Kayak(codigo,participantes,distanciaKm,duracionHoras);
        }else{
            throw new PersistenciaException("Tipo de excursión inválida.");
        }

        excursion.setId(id);
        return excursion;
    }
}
