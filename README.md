# Game Platform API

Backend API for a game commerce platform built with Spring Boot. The project explores production-oriented API design through catalog management, inventory, authentication, external integrations and automated testing.

The repository is also used as an engineering sandbox for evolving a conventional Spring application toward clearer boundaries, stronger tests and maintainable integrations.

## 🛠️ Tecnologias Utilizadas
- **Framework:** Spring Boot, Spring Data JPA
- **Segurança:** Spring Security, JWT
- **Banco de dados:** PostgreSQL
- **Testes:** JUnit, Mockito
- **Qualidade:** SonarCloud integration
- **Documentação:** Swagger/OpenAPI
- **Deploy:** Docker, Heroku

## 🚀 Demonstração
```json
// POST /api/games - Criar novo jogo
{
  "nome": "The Legend of Zelda: Breath of the Wild",
  "descricao": "Aventura épica em mundo aberto",
  "preco": 299.99,
  "plataforma": "Nintendo Switch",
  "categoria": {
    "id": 1,
    "nome": "Aventura"
  }
}

// GET /api/games - Listar jogos
{
  "content": [
    {
      "id": 1,
      "nome": "The Legend of Zelda: Breath of the Wild",
      "preco": 299.99,
      "categoria": "Aventura",
      "disponivel": true
    }
  ]
}
```

## 📁 Estrutura do Projeto
```
game-platform-api/
├── src/main/java/
│   ├── controller/               # REST Controllers
│   │   ├── GameController.java   # Endpoints de jogos
│   │   ├── CategoryController.java # Endpoints de categorias
│   │   └── UserController.java   # Endpoints de usuários
│   ├── model/                    # Entidades JPA
│   │   ├── Game.java            # Entidade Jogo
│   │   ├── Category.java        # Entidade Categoria
│   │   └── User.java            # Entidade Usuário
│   ├── repository/               # Repositórios JPA
│   ├── service/                  # Lógica de negócio
│   ├── security/                 # Configurações de segurança
│   └── config/                   # Configurações gerais
├── src/test/                     # Testes automatizados
└── target/                       # Arquivos compilados
```

## 💡 Principais Aprendizados

### 🛍️ E-commerce Architecture
- **Product management:** CRUD completo de produtos
- **Category organization:** Hierarquia de categorias
- **Inventory control:** Controle de estoque
- **Search functionality:** Busca e filtros avançados
- **Price management:** Gestão de preços e promoções

### 🔐 Security Implementation
- **JWT Authentication:** Autenticação baseada em tokens
- **Role-based access:** Controle de acesso por perfis
- **Password encryption:** Criptografia de senhas
- **CORS configuration:** Configuração para frontend
- **API security:** Proteção de endpoints sensíveis

### 📊 Data Management
- **JPA relationships:** Relacionamentos OneToMany/ManyToOne
- **Query optimization:** Consultas otimizadas
- **Transaction management:** Controle transacional
- **Data validation:** Validação de dados de entrada
- **Error handling:** Tratamento consistente de erros

## 🧠 Conceitos Técnicos Estudados

### 1. **Entity Relationships**
```java
@Entity
@Table(name = "games")
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 100)
    private String nome;
    
    @Column(columnDefinition = "TEXT")
    private String descricao;
    
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal preco;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoria_id")
    private Category categoria;
    
    // Constructors, getters, setters
}

@Entity
@Table(name = "categories")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true)
    private String nome;
    
    @OneToMany(mappedBy = "categoria", cascade = CascadeType.ALL)
    private List<Game> games = new ArrayList<>();
}
```

### 2. **REST Controller Design**
```java
@RestController
@RequestMapping("/api/games")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class GameController {
    
    @Autowired
    private GameService gameService;
    
    @GetMapping
    public ResponseEntity<List<Game>> getAll() {
        return ResponseEntity.ok(gameService.findAll());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Game> getById(@PathVariable Long id) {
        return gameService.findById(id)
            .map(game -> ResponseEntity.ok(game))
            .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<Game> create(@Valid @RequestBody Game game) {
        Game savedGame = gameService.save(game);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedGame);
    }
    
    @GetMapping("/categoria/{categoria}")
    public ResponseEntity<List<Game>> getByCategory(@PathVariable String categoria) {
        List<Game> games = gameService.findByCategoriaNomeContainingIgnoreCase(categoria);
        return ResponseEntity.ok(games);
    }
}
```

### 3. **Service Layer**
```java
@Service
public class GameService {
    
    @Autowired
    private GameRepository gameRepository;
    
    @Autowired
    private CategoryService categoryService;
    
    public List<Game> findAll() {
        return gameRepository.findAll();
    }
    
    public Optional<Game> findById(Long id) {
        return gameRepository.findById(id);
    }
    
    @Transactional
    public Game save(Game game) {
        // Validar categoria
        if (game.getCategoria() != null && game.getCategoria().getId() != null) {
            Category categoria = categoryService.findById(game.getCategoria().getId())
                .orElseThrow(() -> new EntityNotFoundException("Categoria não encontrada"));
            game.setCategoria(categoria);
        }
        
        return gameRepository.save(game);
    }
    
    public List<Game> findByCategoriaNomeContainingIgnoreCase(String categoria) {
        return gameRepository.findByCategoriaNomeContainingIgnoreCase(categoria);
    }
}
```

## 🚧 Desafios Enfrentados
1. **Data modeling:** Modelagem eficiente de relacionamentos
2. **Performance:** Otimização de queries com JPA
3. **Security:** Implementação robusta de autenticação
4. **Error handling:** Tratamento consistente de exceções
5. **Testing:** Cobertura adequada de testes

## 📚 Recursos Utilizados
- [Spring Boot Reference](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/)
- [Spring Data JPA](https://docs.spring.io/spring-data/jpa/docs/current/reference/html/)
- [Spring Security](https://docs.spring.io/spring-security/site/docs/current/reference/html5/)
- [E-commerce Best Practices](https://www.baeldung.com/spring-boot-ecommerce)
- [Generation Brasil Bootcamp](https://brazil.generation.org/) - Bootcamp onde o projeto foi desenvolvido

## 📈 Próximos Passos
- [ ] Implementar sistema de carrinho de compras
- [ ] Adicionar sistema de avaliações
- [ ] Criar sistema de recomendações
- [ ] Implementar processamento de pagamentos
- [ ] Adicionar sistema de cupons de desconto
- [ ] Criar dashboard administrativo

## 🔗 Projetos Relacionados
- [React GameStore Front](../react-gamestore-front/) - Frontend da aplicação

---

**Desenvolvido por:** Felipe Macedo  
**Contato:** contato.dev.macedo@gmail.com  
**GitHub:** [FelipeMacedo](https://github.com/felipemacedo1)  
**LinkedIn:** [felipemacedo1](https://linkedin.com/in/felipemacedo1)

> 💡 **Reflexão:** Este projeto consolidou conhecimentos em desenvolvimento de APIs REST para e-commerce. A implementação de relacionamentos JPA e sistema de autenticação proporcionou experiência prática em arquiteturas backend robustas.