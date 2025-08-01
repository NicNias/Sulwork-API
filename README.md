<article>
  <h1 align='center'>Sulwork API</h1>

  <h3>Sobre:</h3>
  <p>O desafio consistiu em desenvolver uma aplicação para cadastrar um novo colaborador para o café da manhã, excluí-lo, editar suas informações e status.</p>

  <h3>Tecnologias Back-end:</h3>
  <ul>
    <li>Java</li>
    <li>Spring Boot</li>
    <li>Lombok</li>
    <li>JPA</li>
    <li>MapStructor</li>
    <li>Swagger</li>
    <li>Junit5</li>
    <li>Mockito</li>
  </ul>

  <h3>Ferramentas:</h3>
  <ul>
    <li>Docker</li>
  </ul>
  
  <h3>Tutorial Back-end:</h3>
  <ol>
    <li>
      Execute o comando:
      <code>git clone https://github.com/NicNias/Sulwork-API.git</code>
    </li>
    <li>
      Variáveis para testes e para rodar a aplicação localmente: <br>
      <code>spring.datasource.url=${SPRING_DATASOURCE_URL}</code> <br>
      <code>spring.datasource.username=${SPRING_DATASOURCE_USERNAME}</code> <br>
      <code>spring.datasource.password=${SPRING_DATASOURCE_PASSWORD}</code>
      <br>
      Para o ambiente Docker, deve-se criar um arquivo <code>.env</code> copiando as variáveis do arquivo <code>.env.example</code>.
    </li>
    <li>
      Construa e inicie os containers usando o Docker Compose:
      <code>docker-compose up -d</code>
    </li>
    <li>
      Acesse o Swagger pela URL: <code>http://localhost:8081/swagger-ui/index.html</code>
      <br /> <br />
      <strong>Observação:</strong> Se estiver executando a aplicação via <code>Docker</code>, use a porta <code>8081</code>. 
      Caso esteja rodando localmente (sem Docker), utilize a porta <code>8080</code>: <code>http://localhost:8080/swagger-ui/index.html</code>
    </li>
  </ol>
</article>
