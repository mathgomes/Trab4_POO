# Trab4_POO


Matheus Gomes da Silva Horta - 8532321
Lais Helena Chiachio de miranda - 8517032
Gabriela Duque - 7694220

## Sobre o sistema :

Sistema Operacional : Windows 8.1
IDE utilizada : Intellij IDEA
Java versão : 1.8.0_31

## Sobre a execução :

Execute as classes "ClientGUI.java" e "ServerGUI.java" ou
execute os arquivos .JAR clicando duas vezes em cada um.
Obs: Se algum jar não executar, o unico jeito passa a ser na execução do programa.


### Na aplicação cliente :

Primeiro, digite o IP ao qual se deseja conectar, na textFiel.
Depois, siga para o cadastro ou login.

Cadastrar usuário : Preencha os campos da parte direita da area do usuário e clique no botão " Criar conta"

Fazer login : Prencha os campos da parte esquerda da area do usuário e clique no botão " Logar mo sistema "

Listar itens : Clique no botão "Listar produtos". Os produtos serão mostrados na textArea.

Realizar Compra : Clique no botão "Realizar Compra" e digite o nome do item que deseja comprar no textField.

LogOut : Clique no botão "logOut" e a aplicação se encerrará

### Na aplicação servidor :

Registrar Produto : Clique no botão "Registrar Produto" e digite as informações do produto no textField.

Listar Produtos Disponiveis : Clique no botão "Listar Produtos Disponiveis". Os produtos aparecerão na textArea.

Listar Produtos Indisponíveis : Clique no botão "Listar Produtos Indiponiveis". Os produtos aparecerão na teztArea.

Gerar PDF de vendas : Clique no botão "Gerar PDF de vendas" e digite no textField o prefixo "dia," + o dia na forma numérica que deseja
gerar as vendas( para o mes seria "mes,"). Então será gerado o arquivo PDF com as informacoes pedidas.


## Sobre o projeto :

- Foi usado o padrão de projeto singleton para a implementação do servidor. A classe Server contem um objeto dela
mesma e um método que chama esse objeto, sendo assim, apenas 1 objeto do tipo Server é utilizado em todo o programa.
Isso é util pois, proporciona acesso global das informações do servidor ( como lista de produtos e usuarios ) a todas as
threads que cuidam dos clientes.

- Ao se cadastrar um novo item no servidor, por conveniencia ele é criado com quantidade 1.
- O fornecedor demora de 10 a 20s para incrementar o estoque de um item esgotado( analogo ao tempo de transporte ).
- O arquivo information.csv ja comtem alguns clientes, itens, fornecedores e vendas para auxiliar nos testes.
- Para que as informações sejam salvas no arquivo, é necessario clicar no botão de logOut do servidor ao sair da aplicação.
- Clientes só podem comprar 1 item por vez.
- As datas são medidas em tempo real.
- JANEIRO É O MES 0 e DEZEMBRO O MES 11