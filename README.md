# Aplicatie de gestionare a mersului trenurilor

Aplicația își propune să le ofere utilizatorilor posibilitatea de a căuta și achiziționa bilete de tren, precum și
de a le gestiona, modifica sau anula.

# Functionalitati
1. **Crearea și gestionarea conturilor**
    - Utilizatorii pot crea un cont nou, se pot autentifica sau își pot actualiza datele personale dacă dețin deja un cont.

2. **Căutarea rutelor și biletelor disponibile**
    - Oricine poate căuta o rută accesând pagina principală pentru a verifica disponibilitatea biletelor.

3. **Sugestii pentru rute indirecte**
    - Dacă nu există o conexiune directă între două stații, aplicația va sugera un itinerar format din mai multe stații intermediare. Utilizatorii vor trebui să achiziționeze separat bilete pentru fiecare segment al traseului.

4. **Redirecționare către login**
    - În cazul în care un utilizator dorește să cumpere un bilet, dar nu este autentificat, acesta va fi redirecționat automat către pagina de login.

5. **Procesul de achiziționare a biletelor**
    - Atunci când un bilet este achiziționat, utilizatorul trebuie să specifice detalii precum locul, vagonul și clasa dorite.
    - Sistemul verifică dacă:
        - Informațiile furnizate sunt corecte.
        - Locul selectat este deja ocupat.
        - Mai există locuri disponibile în tren.

6. **Gestionarea biletelor achiziționate**
    - În secțiunea „Cont”, utilizatorii pot vizualiza biletele achiziționate.
    - Aceste bilete pot fi modificate sau anulate, dar modificările sunt supuse acelorași validări (ex.: verificarea disponibilității locului nou ales, corectitudinea datelor furnizate etc.).

7. **Tipuri de conturi**
    - Aplicația permite existența a trei tipuri de conturi:
        - **Pasager**
        - **Personal**
        - **Admin**

8. **Funcționalități specifice fiecărui tip de cont**
    - **Pasager**:
        - Poate cumpăra și modifica bilete.
    - **Personal** (angajații gării):
        - Poate cumpăra și vizualiza bilete.
        - Are acces la informații despre trenurile la care a fost asignat.
    - **Admin**:
        - Dispune de cele mai multe privilegii:
            - Poate aloca trenuri angajaților prin intermediul unei interfețe grafice.
            - Poate adăuga, modifica sau șterge trenuri, rute, programe etc. (momentan, aceste funcționalități nu sunt disponibile prin interfața grafică).


# Descriere clase/metode/atribute

1. **Utilizator**
    - **Clasa pentru Utilizator**
    - **Atribute**:
        - **utilizatorId**: id-ul utilizatorului in baza de date
        - **nume**: numele de tip String
        - **prenume**: prenumele de tip String
        - **email**: email-ul de tip string ce trebuie sa aiba un format specific (ex: john@example.com)
        - **telefon**: telefonul de tip String (obligatoriu 10 cifre si incepe cu 07)
        - **parola**: parola de tip String (minim 8 caractere, minim o litera mare si una mica, minim o cifra si un caracter
          special)
        - **rol**: specifica tipul contului: pasager, personal, admin
        - **tren**: nenul doar daca contul este de tip personal si indica trenul asignat unui angajat
    - **Id-ul se autoincrementeaza**
    - **Clasa implementeaza interfata UserDetails care o sa ne ajute mai tarziu cu autentificarea contului**
    - **Restul fisierelor din package-ul utilizator constituie arhitectura MVC**
    - **Descrierea API-ului**
        - ruta /api/utilizator/
            - GET: returneaza un obiect de tip Utilizator dupa id (ex: /api/utilizator/id=2)
            - PUT: modifica un utilizator (ex: /api/utilizator/id=1&nume=Doe&prenume=John&email=john.@example.com
              &telefon=0737463459)
            - DELETE: sterge utilizator dupa id, aceeasi sintaxa ca la GET
        - ruta /api/utilizator/getId
            - GET: returneaza id-ul unui utilizator dupa email (ex: /api/utilizator/getId/email=john@example.com)
        - ruta /api/utilizator/bilet
            - POST: inregistreaza un bilet unui utilizator (ex: /api/utilizator/bilet/id=2&program=3). In corpul request-ului trebuie sa se
              afle un obiect de tip Bilet (sa contina campurile loc, vagon, clasa si pret)
        - ruta /api/utilizator/tren
            - POST: asigneaza unui utilizator cu cont de tip personal un tren (ex: /api/utilizator/tren/id=2&tren=6)
        - Pentru a accesa oricare dintre aceste rute este nevoie de autentificare (in request trebuie sa se afle un Bearer
          token primit in momentul autentificarii). Un cont nu poate sa vizualizeze sau sa modifice alte conturi/
2. **Bilet**
    - **Clasa pentru bilet**
    - **Atribute**
      - **biletId**: id-ul biletului in baza de date
      - **vagon**: numarul vagonul de tip Integer
      - **loc**: locul in tren de tip Integer
      - **pret**: pretul biletului de tip Double
      - **clasa**: clasa la care se cumpara bilet de tip Integer (poate lua valorile 1 sau 2)
      - **utilizator**: indica utilizatorul care detine biletul
      - **program**: indica programul asociat biletului (data plecarii si sosirii + informatii referitoare la tren si la ruta)
   - **Id-ul se autoincrementeaza**
   - **Restul fisierelor din package-ul bilet constituie arhitectura MVC**
   - **Descrierea API-ului**
     - ruta /api/bilet/
       - GET: Returneaza un obiect de tip bilet dupa id-ul lui si id-ul utilizatorului care il detine 
(ex: /api/bilet/id=2&utilizator=3)
       - DELETE: sterge un bilet dupa id-ul lui si id-ul utilizatorului care il detine (aceeasi sintaxa ca la GET)
       - PUT: modifica detaliile unui bilet (ex: /api/bilet/id=1&loc=2&vagon=1&clasa=2&pret=50&utilizator=3)
     - ruta /api/bilet/cauta
        - GET: returneaza toate biletele unui utilizator dupa id-ul utilizatorului (ex: /api/bilet/cauta/id=3)
     - Rutele sunt accesibile doar pentru utilizatorii logati (necesita Bearer token)
3. **Tren**
   - **Clasa pentru tren**
   - **Atribute**
     - **trenId**: id-ul trenului in baza de date
     - **denumire**: denumirea trenului de tip String
     - **capacitate**: capacitatea totala a trenului de tip Integer
     - **numarVagoane**: numarul de vagoane de tip Integer
   - **Id-ul se autoincrementeaza**
   - **Restul fisierelor din package-ul tren constituie arhitectura MVC**
   - **Descrierea API-ului**
     - ruta /api/tren
       - GET: fara parametru returneaza toate trenurile. Cu parametru returneaza un obiect de tip tren dupa id 
(ex: /api/tren/id=2)
       - POST: creaza un tren in baza de date. In corpul request este necesar un obiect de tip tren.
       - DELETE: sterge un tren dupa id (aceeasi sintaxa ca la GET)
       - PUT: modifica informatiile unui tren (ex: /api/tren/id=2&denumire=RE502&capacitate=210&numarVagoane=1)
     - ruta /api/tren/program
       - GET: returneaza orarul unui tren (ex: /api/tren/program/denumire=RE502)
     - Rutele sunt accesibile doar de utilizatorii cu rol de admin
4. **Ruta**
   - **Clasa pentru Ruta**
   - **Atribute**
     - **rutaId**: id-ul rutei in baza de date
     - **statiePlecare**: statie de plecare a trenului de tip String
     - **statieDestinatie**: statie destinatie a trenului de tip String
     - **distanta**: distanta dintre statii de tip Integer
     - **durata**: durata calatoriei de tip Integer
   - **Id-ul se autoincrementeaza**
   - **Restul fisierelor din package-ul ruta constituie arhitectura MVC**
   - **Descrierea API-ului**
     - ruta /api/ruta
       - GET: fara parametru returneaza toate rutele
       - POST: inregistreaza o ruta noua
       - PUT: modifica o ruta (ex: /api/ruta/id=1&plecare=Bucuresti&destinatie=Timisoara&distanta=500&durata=5)
       - DELETE: sterge o ruta dupa id (ex: /api/ruta/id=2)
     - ruta /api/ruta/program
       - POST: inregistreaza un program pentru o ruta (ex: /api/ruta/program/id=1&tren=2). Sunt necesare id-ul rutei, 
id-ul trenului si in corpul request-ului trebuie sa se afle un obiect de tip program
     - ruta /api/ruta/cautare
       - GET: returneaza toate informatiile referitoare la trenurile care pleaca dint-o statie si 
ajung in alta (ex: /api/ruta/cautare/plecare=Pitesti&destinatie=Bucuresti)
     - ruta /api/ruta/cautare/statii
       - GET: returneaza un toate statiile prin care trebuie sa treci pentru a ajunge la destinatie (ex: /api/ruta/cautare/statii/plecare=Pitesti&destinatie=Timisoara)
     - determinarea statiilor intermediare cand nu exista tren direct se face utilizand algoritmul lui Dijkstra. Pentru
s-a folosit clasa Graph.
     - rutele ce au ca scop cautarea sunt accesibile de toata lumea
5. **Program**
   - **Clasa pentru program**
   - **Atribute**
     - **programId**: id-ul programului in baza de date
     - **dataPlecare**: data plecarii trenului de tip Date
     - **dataAjungere**: data sosirii trenului de tip Date
     - **ruta**: indica ruta atribuita programului
     - **tren**: indica trenul atribuit programului
   - **Id-ul se autoincrementeaza**
   - **Restul fisierelor din package-ul program constituie arhitectura MVC**
   - **Descrierea API-ului**
     - ruta /api/program
       - GET: fara parametru returneaza toate programele. Cu parametru returneaza un program dupa id (ex: /api/program/id=2)
       - PUT: modifica un program (ex: /api/program/id=2&dataPlecare=2025-01-01T12:00&dataAjungere=2025-01-01T14:00&trenId=5)
       - DELETE: sterge un program (aceeasi sintaxa ca la GET)
       - Rutele sunt accesibile doar de catre admini
6. **Auth si config**
   - package-uri responsabile cu autentificarea si securizarea rutelor
   - **Descriere clase**
     - **ApplicationConfig si SecurityConfig**: responsabile cu securizarea rutelor cat si configurarea functiilor necesare autentificarii
     - **JwtService**: clasa care contine metodele principale (ex: generare de token, extragerea email-ului din token, verificarea token-ului etc.) pentru lucrul cu JWT (folositi aici ca token de autentificare)
     - **JwtAuthFilter**: configurarea filtrului de autentificare
     - **AuthRequest**: clasa care descrie cum trebuie sa arate o cerere de autentificare
     - **AuthResponse**: clasa care descrie cum trebuie sa arate un raspuns al unei cereri de autentificare
     - **RegisterRequest**: clasa care descrie cum trebuie sa arate o cerere de inregistrare
     - **AuthController si AuthService**: responsabile cu autentificarea
       - POST /api/auth/register: creaza un cont nou (request-ul trebuie sa aiba in body un obiect de tip RegisterRequest)
       - POST /api/auth/authenticate: autentifica un cont (request-ul trebuie sa aiba in body un obiect de tip AuthRequest)
       - ambele rute returneaza un raspuns de tip AuthResponse (care si el contine token-ul de autentificare). Daca autentificarea 
nu a functionat, acesta returneaza un mesaj corespunzator

# Elemente functionale oferite de interfata grafica
1. **Home (/)**
   - Pagina principala
   - Aici putem cauta bilete pentru tren
   - Pentru a cauta bilete completam campurile "Statie de plecare" si "Statie destinatie" si dupa apasam Enter sau pe butonul Cauta
   - Daca exista tren direct intre localitatile introduse, acestea vor aparea sub forma de table unde vor fi afisate informatii
referitoare la data plecarii, data sosirii, tren si un buton "Cumpara" pe care daca apasam putem sa introducem datele biletului
   - Daca nu exista tren direct atunci se va afisa un mesaj referitor la acest lucru si eventual un traseu recomandat
   - Daca utilizatorul va dori sa cumpere un bilet fara sa fie logat, acesta va fi redirectionat catre pagina de logare
2. **Login (/login) si Signup (/signup)**
   - Paginile de login si signup
   - Aici utilizatorul se poate loga sau inregistra
   - Daca vreun camp introdus de catre utilizator nu este corect se va furniza un mesaj de informare asupra acestui lucru
3. **Cont (/cont)**
   - Pe aceasta pagina utilizatorul poate vizualiza detalii referitoare la contul sau, inclusiv asupra biletelor
   - Prin apasarea butonului Modifica acesta isi poate modifica datele contului
   - La sectiunea Bilete apare un table unde sunt trecute toate biletele acestuia. Acestea pot fi eventual modificate prin apasarea butonului Vizualizare
   - La conturile cu rol de admin apare butonul Asigneaza tren prin intermediul caruia acestia pot sa asigneze un tren unei persoane angajate
4. **Cumparare (/cumparare)**
    - Pagina unde furnizam informatiile necesare cumpararii unui bilet
    - Aplicatia verifica si ofera un mesaj relevant in cazul in care informatiile furnizate nu sunt corecte (loc indisponibil, trenul nu mai are locuri etc.)
5. **Modificare bilet (/modificare)**
   - Pagina unde putem modifca sau sterge un bilet
6. **Asignare Tren (/asignare)**
   - Pagina unde un admin poate sa signeze un tren unui angajat
7. **Delogarea se face prin apasarea butonului Logout din bara de navigatie**