SurfStatus
==========

## Objectivo
Aplicação Android para, numa única vista, ver o estado de várias praias favoritas evitando ter de abrir várias páginas diferentes a partir do browser.
* [http://beachcam.meo.pt/reports/praia-do-moledo/]
* [http://beachcam.meo.pt/reports/praia-da-mariana/]
* [lista completa](http://beachcam.meo.pt/reports/) 

## Funcionalidades
Acede à página web bechcam.meo.pt-reports, interpreta o HTML, gera uma lista com as praias disponíveis e permite seleccionar as favoritas.
Na base de dados local serão armazenados o nome da praia, o estado da praia e o url das praias favoritas.
A partir do ecrã principal (praias favoritas), é apresentado o último estado de cada uma (Bom, Mau, Razoável) e é possível actualizar o mesmo.
Também no ecrã principal, ao clicar numa praia é feita uma ligacao à página da respectiva praia e será apresentada a descrição detalhada das condições.

## Bugs
* No ecrã de escolha das praias a apresentar, é necessário clicar e não deslizar o botão (possivelmente depende da versão do Android);
* ~~Se for usada a tecla de retorno do Android e não o menu, a aplicação pode deixar de funcionar;~~
* ~~Durante a operação de recolha de dados (asynctask e escrever na BD), se esta for interrompida, a aplicação pode deixar de funcionar;~~
* ~~No caso de não haver ligação à internet, a aplicação pode deixar de funcionar.~~
