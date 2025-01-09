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
            - GET: inregistreaza un bilet unui utilizator (ex: /api/utilizator/bilet/id=2&program=3). In corpul request-ului trebuie sa se
              afle un obiect de tip Bilet (sa contina campurile loc, vagon, clasa si pret)
        - ruta /api/utilizator/tren
            - GET: asigneaza unui utilizator cu cont de tip personal un tren (ex: /api/utilizator/tren/id=2&tren=6)
        - Pentru a accesa oricare dintre aceste rute este nevoie de autentificare (in request trebuie sa se afle un Bearer
          token primit in momentul autentificarii). Un cont nu poate sa vizualizeze sau sa modifice alte conturi/
