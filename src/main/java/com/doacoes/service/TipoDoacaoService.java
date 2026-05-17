    package com.doacoes.service;

    import com.doacoes.dao.TipoDoacaoDAO;
    import com.doacoes.model.TipoDoacao;

    import javax.swing.JOptionPane;
    import java.util.List;

    public class TipoDoacaoService {

        private final TipoDoacaoDAO dao = new TipoDoacaoDAO();

        public boolean salvar(TipoDoacao tipoDoacao) {


            if (tipoDoacao.getNome() == null || tipoDoacao.getNome().isBlank()) {

                JOptionPane.showMessageDialog(
                        null,
                        "Nome inválido!"
                );

                return false;
            }


            if (!tipoDoacao.getNome().matches("[a-zA-ZÀ-ÿ\\s]+")) {

                JOptionPane.showMessageDialog(
                        null,
                        "O nome não pode conter números!"
                );

                return false;
            }

            dao.salvar(tipoDoacao);

            return true;
        }

        public List<TipoDoacao> listar() {
            return dao.listar();

        }

        public TipoDoacao buscarPorId(Long id) {
            return dao.buscarPorId(id);
        }

        public boolean atualizar(TipoDoacao tipoDoacao) {
            if (tipoDoacao.getNome() == null || tipoDoacao.getNome().isBlank()) {

                JOptionPane.showMessageDialog(
                        null,
                        "Nome inválido!"
                );

                return false;
            }


            if (!tipoDoacao.getNome().matches("[a-zA-ZÀ-ÿ\\s]+")) {

                JOptionPane.showMessageDialog(
                        null,
                        "O nome não pode conter números!"
                );

                return false;
            }

            dao.salvar(tipoDoacao);

            return true;
        }




        public void deletar(Long id) {
            TipoDoacao tipo = dao.buscarPorId(id);

            if (tipo == null) {
                JOptionPane.showMessageDialog(null, "Tipo de doação não encontrado!");
                return;
            }

            dao.deletar(id);
        }}
