package alejandro.foro_hub.Domain.Models;

import lombok.Getter;

@Getter
public enum Categoria {
    DESARROLLO_DE_SOFTWARE ("Desarrollo de software"),
    POLÍTICA ("Política"),
    SALUD ("Salud"),
    MATEMATICAS ("Matemáticas"),
    ALGORITMIA ("Algorítmia"),
    LINUX ("Linux"),
    CIBERSEGURIDAD ("Ciberseguridad"),
    HACKING_ETICO ("Hacking ético"),
    ELECTRICIDAD ("Electricidad"),
    HEURISTICAS ("Heurísticas"),
    METAHEURISTICAS ("Metaheurísticas"),
    COMPUTACION_BASICA ("Computación básica"),
    DESARROLLO_FRONTEND ("Desarrollo frontend"),
    DESARROLLO_BACKEND ("Desarrollo backend"),
    ADOBE_PHOTOSHOP ("Adobe photoshop"),
    ADOBE_PREMIER ("Adobe premier"),
    DEVOPS_AZURE ("Devops Azure"),
    DEVOPS_AWS ("Devops AWS");

    private final String categoria;

    Categoria (String categoria){
        this.categoria = categoria;
    }
}
