package loolu.loolu_backend.models;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import loolu.loolu_backend.repositories.PictureRepository;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Schema(description = "Product's name", example = "Computer")
    @NotBlank(message = "Product's name is mandatory")
    @Column(name = "title", nullable = false)
    private String title;

    @Schema(description = "Product's price", example = "123,00")
    @NotBlank(message = "Product's price is mandatory")
    @Column(name = "price", nullable = false)
    private Double price;

    @Schema(description = "Product's description", example = "Product's description")
    @NotBlank(message = "Product's description is mandatory")
    @Column(name = "description", nullable = false)
    private String description;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Picture> picture = new HashSet<>();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @ManyToMany(mappedBy = "products")
    private Set<Cart> carts = new HashSet<>();

    // Переопределяем hashCode и equals, исключая поле picture для избежания циклической зависимости
    @Override
    public int hashCode() {
        return Objects.hash(id, title, price, description, category);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Objects.equals(id, product.id) &&
                Objects.equals(title, product.title) &&
                Objects.equals(price, product.price) &&
                Objects.equals(description, product.description) &&
                Objects.equals(category, product.category);
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", price=" + price +
                ", description='" + description + '\'' +
                ", category=" + category +
                '}';
    }
}
