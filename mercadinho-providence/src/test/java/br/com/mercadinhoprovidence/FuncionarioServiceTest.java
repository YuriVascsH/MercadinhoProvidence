// package br.com.mercadinhoprovidence;

// import br.com.mercadinhoprovidence.Service.FuncionarioService;
// import br.com.mercadinhoprovidence.dao.FuncionarioDao;
// import br.com.mercadinhoprovidence.dto.FuncionarioTableDto;
// import br.com.mercadinhoprovidence.model.Funcionario;
// import br.com.mercadinhoprovidence.model.enums.Cargo;
// import org.junit.jupiter.api.Test;
// import org.junit.jupiter.api.extension.ExtendWith;
// import org.mockito.InjectMocks;
// import org.mockito.Mock;
// import org.mockito.junit.jupiter.MockitoExtension;

// import java.math.BigDecimal;
// import java.time.LocalDate;
// import java.util.ArrayList;
// import java.util.List;

// import static org.junit.jupiter.api.Assertions.assertEquals;
// import static org.mockito.Mockito.when;

// @ExtendWith(MockitoExtension.class)
// public class FuncionarioServiceTest {

//     @Mock
//     private FuncionarioDao funcionarioDao;

//     @InjectMocks
//     private FuncionarioService funcionarioService;

//     @Test
//     void deveRetornarListaDeFuncionarioTableDto() {

//         Funcionario f1 = new Funcionario();
//         f1.setIdFuncionario(1);
//         f1.setCodigoVerificador(101);
//         f1.setNome("Marcos Silva");
//         f1.setCpf("12345678901");
//         f1.setCargo(Cargo.GERENTE);
//         f1.setSalario(new BigDecimal("4500.00"));
//         f1.setDataAdmissao(LocalDate.of(2020, 1, 10));
//         f1.setAtivo(true);

//         Funcionario f2 = new Funcionario();
//         f2.setIdFuncionario(2);
//         f2.setCodigoVerificador(102);
//         f2.setNome("Ana Pereira");
//         f2.setCpf("98765432100");
//         f2.setCargo(Cargo.OPERADOR);
//         f2.setSalario(new BigDecimal("2200.00"));
//         f2.setDataAdmissao(LocalDate.of(2021, 5, 20));
//         f2.setAtivo(true);

//         List<Funcionario> funcionarios = List.of(f1, f2);

//         when(funcionarioDao.listarTodos()).thenReturn(funcionarios);

//         List<FuncionarioTableDto> resultado = funcionarioService.buscarTodosOsFuncionarios();

//         assertEquals(2, resultado.size());

//         // Primeiro funcionário
//         assertEquals(1, resultado.getFirst().getIdFuncionario());
//         assertEquals("Marcos Silva", resultado.getFirst().getNome());
//         assertEquals("12345678901", resultado.getFirst().getCpf());
//         assertEquals(Cargo.GERENTE, resultado.getFirst().getCargo());

//         // Segundo funcionário
//         assertEquals(2, resultado.get(1).getIdFuncionario());
//         assertEquals("Ana Pereira", resultado.get(1).getNome());
//         assertEquals("98765432100", resultado.get(1).getCpf());
//         assertEquals(Cargo.OPERADOR, resultado.get(1).getCargo());
//     }

    
// }
