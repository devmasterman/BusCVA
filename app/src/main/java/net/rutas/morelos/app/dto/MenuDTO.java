package net.rutas.morelos.app.dto;

/**
 * Created by eroman on 25/10/16.
 */

public class MenuDTO {

    private String titulo;
    private String subtitulo;
    private int imagen;



    /**
     *
     */
    public MenuDTO() {
        super();
        // TODO Auto-generated constructor stub
    }
    /**
     * @param titulo
     * @param subtitulo
     * @param imagen
     */
    public MenuDTO(String titulo, String subtitulo, int imagen) {
        this.titulo = titulo;
        this.subtitulo = subtitulo;
        this.imagen = imagen;
    }
    /**
     * @return the titulo
     */
    public String getTitulo() {
        return titulo;
    }
    /**
     * @param titulo the titulo to set
     */
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
    /**
     * @return the subtitulo
     */
    public String getSubtitulo() {
        return subtitulo;
    }
    /**
     * @param subtitulo the subtitulo to set
     */
    public void setSubtitulo(String subtitulo) {
        this.subtitulo = subtitulo;
    }
    /**
     * @return the imagen
     */
    public int getImagen() {
        return imagen;
    }
    /**
     * @param imagen the imagen to set
     */
    public void setImagen(int imagen) {
        this.imagen = imagen;
    }
}
