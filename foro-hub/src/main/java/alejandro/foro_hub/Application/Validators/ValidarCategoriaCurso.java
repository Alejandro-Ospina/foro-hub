package alejandro.foro_hub.Application.Validators;

import alejandro.foro_hub.Application.DTOs.CursoDTO;
import alejandro.foro_hub.Domain.Exceptions.CategoriaException;
import alejandro.foro_hub.Domain.Models.Categoria;
import jakarta.persistence.EntityExistsException;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class ValidarCategoriaCurso implements Validador<CursoDTO> {

    @SneakyThrows
    @Override
    public void validar(CursoDTO dto) throws EntityExistsException {
        boolean existeCategoria = Arrays.stream(Categoria.values()).anyMatch(
                categoria -> categoria.getCategoria().equalsIgnoreCase(dto.categoria().getCategoria())
        );

        if (!existeCategoria)
            throw new CategoriaException("No existe una categoría asociada a: " + dto.categoria().getCategoria());
    }
}
