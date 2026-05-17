package com.doacoes.dao;

import com.doacoes.connection.ConnectionFactory;
import com.doacoes.enums.CategoriaItem;
import com.doacoes.model.TipoDoacao;
import org.jetbrains.annotations.NotNull;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TipoDoacaoDAO {

    public void salvar(@NotNull TipoDoacao tipoDoacao) {

        String sql = "INSERT INTO tipo_doacao(nome, categoria) VALUES (?, ?)";

        try (
                Connection connection = ConnectionFactory.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {

            statement.setString(1, tipoDoacao.getNome());
            statement.setString(2, tipoDoacao.getCategoria().name());

            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<TipoDoacao> listar() {

        List<TipoDoacao> lista = new ArrayList<>();

        String sql = "SELECT * FROM tipo_doacao";

        try (
                Connection connection = ConnectionFactory.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery()
        ) {

            while (resultSet.next()) {

                TipoDoacao tipo = new TipoDoacao();

                tipo.setId(resultSet.getLong("id"));
                tipo.setNome(resultSet.getString("nome"));
                tipo.setCategoria(
                        CategoriaItem.valueOf(resultSet.getString("categoria"))
                );

                lista.add(tipo);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return lista;
    }

    public TipoDoacao buscarPorId(Long id) {

        String sql = "SELECT * FROM tipo_doacao WHERE id = ?";

        try (
                Connection connection = ConnectionFactory.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {

            statement.setLong(1, id);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {

                TipoDoacao tipo = new TipoDoacao();

                tipo.setId(resultSet.getLong("id"));
                tipo.setNome(resultSet.getString("nome"));
                tipo.setCategoria(
                        CategoriaItem.valueOf(resultSet.getString("categoria"))
                );

                return tipo;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    public void atualizar(TipoDoacao tipoDoacao) {

        String sql = """
                UPDATE tipo_doacao
                SET nome = ?, categoria = ?
                WHERE id = ?
                """;

        try (
                Connection connection = ConnectionFactory.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {

            statement.setString(1, tipoDoacao.getNome());
            statement.setString(2, tipoDoacao.getCategoria().name());
            statement.setLong(3, tipoDoacao.getId());

            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void deletar(Long id) {

        String sql = "DELETE FROM tipo_doacao WHERE id = ?";

        try (
                Connection connection = ConnectionFactory.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {

            statement.setLong(1, id);

            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}