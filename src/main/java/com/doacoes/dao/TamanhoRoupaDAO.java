package com.doacoes.dao;

import com.doacoes.connection.ConnectionFactory;
import com.doacoes.enums.TipoTamanho;
import com.doacoes.model.TamanhoRoupa;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TamanhoRoupaDAO {

    public boolean salvar(TamanhoRoupa tamanhoRoupa) {

        String sql = "INSERT INTO tamanho_roupa(tamanho, categoria) VALUES (?, ?)";

        try (
                Connection connection = ConnectionFactory.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {

            statement.setString(1, tamanhoRoupa.getTamanho());
            statement.setString(2, tamanhoRoupa.getCategoria().name());

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<TamanhoRoupa> listar() {

        List<TamanhoRoupa> lista = new ArrayList<>();

        String sql = "SELECT * FROM tamanho_roupa";

        try (
                Connection connection = ConnectionFactory.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery()
        ) {

            while (resultSet.next()) {

                TamanhoRoupa tamanho = new TamanhoRoupa();

                tamanho.setId(resultSet.getLong("id"));
                tamanho.setTamanho(resultSet.getString("tamanho"));
                tamanho.setCategoria(
                        TipoTamanho.valueOf(resultSet.getString("categoria"))
                );

                lista.add(tamanho);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return lista;
    }

    public TamanhoRoupa buscarPorId(long id) {

        String sql = "SELECT * FROM tamanho_roupa WHERE id = ?";

        try (
                Connection connection = ConnectionFactory.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {

            statement.setLong(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {

                    TamanhoRoupa tamanho = new TamanhoRoupa();

                    tamanho.setId(resultSet.getLong("id"));
                    tamanho.setTamanho(resultSet.getString("tamanho"));
                    tamanho.setCategoria(
                            TipoTamanho.valueOf(resultSet.getString("categoria"))
                    );

                    return tamanho;
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    public boolean atualizar(TamanhoRoupa tamanhoRoupa) {

        String sql = "UPDATE tamanho_roupa SET tamanho = ?, categoria = ? WHERE id = ?";

        try (
                Connection connection = ConnectionFactory.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {

            statement.setString(1, tamanhoRoupa.getTamanho());
            statement.setString(2, tamanhoRoupa.getCategoria().name());
            statement.setLong(3, tamanhoRoupa.getId());

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean deletar(long id) {

        String sql = "DELETE FROM tamanho_roupa WHERE id = ?";

        try (
                Connection connection = ConnectionFactory.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {

            statement.setLong(1, id);

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}