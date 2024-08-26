A Stock Market Advisor alkalmazás egy egyszerű Java-alapú konzolos program, amely részvények és kriptovaluták adatait kezeli és dolgozza fel. Az alkalmazás fő célja, hogy a felhasználóknak segítséget nyújtson befektetési döntések meghozatalában, valamint lehetőséget biztosítson a részvények és kriptovaluták adatainak lekérdezésére, illetve fájlba írására. A program a pénzügyi eszközök adatait a Yahoo Finance API végpontról kérdezi le, majd adatbázisban tárolja azokat.
Főbb komponensek
Az alkalmazás több fő komponensre tagolódik, melyek közül a legfontosabbak a következők:
1.	App.java - Fő belépési pont (UI réteg)
2.	Model osztályok - Az adatok reprezentálása (Stock, CryptoCurrency)
3.	Repository osztályok - Az adatok tárolásáért és lekérdezéséért felelős (StockRepository, CryptoCurrencyRepository)
4.	Logic osztályok - Az üzleti logika implementálása (StockLogic, CryptoCurrencyLogic)
5.	Exceptions - Egyedi kivételek (WrongInputException)
App.java
Az App.java osztály az alkalmazás belépési pontja, amely kezeli a felhasználói interakciókat a konzolon keresztül. Az osztály több menüpontot is kínál a felhasználóknak, például részvények és kriptovaluták adatainak lekérdezésére, illetve azok fájlba írására.
•	userInput() metódus: Ez a metódus kezeli a felhasználói bemeneteket és indítja el a megfelelő logikai műveleteket. Itt dől el, hogy a felhasználó részvényekkel vagy kriptovalutákkal szeretne dolgozni, és hogy milyen műveletet szeretne végrehajtani.
o	Ha részvényt választ, akkor három opció közül választhat:
1.	Befektetési tanácsadás: A részvények RSI (Relatív Erősség Index) alapján javaslatokat tesz vásárlásra vagy eladásra.
2.	Adatok lekérdezése: A felhasználó által megadott részvény azonosító alapján lekéri és megjeleníti a részvény aktuális adatait.
3.	Adatok fájlba írása: A megadott részvény adatait fájlba menti.
o	Ha kriptovalutát választ, akkor hasonló opciókat kap:
1.	Befektetési tanácsadás: RSI alapján javaslatokat tesz vásárlásra vagy eladásra.
2.	Adatok lekérdezése: Lekéri a megadott kriptovaluta adatait.
3.	Adatok fájlba írása: Mentési lehetőség.
Model osztályok
Az alkalmazás két fő adatmodellt definiál:
•	Stock: A részvények adatait reprezentálja, mint például a részvény szimbólumát, aktuális árát, 52 hetes minimum és maximum árakat, RSI értéket és piaci kapitalizációt.
•	CryptoCurrency: Hasonlóan a Stock osztályhoz, a kriptovaluták adatait tartalmazza, beleértve a szimbólumot, aktuális árat, 52 hetes minimum és maximum árakat, RSI értéket és hosszú nevet.
Repository osztályok
Ezek az osztályok felelősek az adatok tárolásáért és lekérdezéséért az adatbázisból:
•	StockRepository: A részvényekkel kapcsolatos adatbázis műveleteket valósítja meg, például beszúrás, frissítés, törlés és lekérdezés.
•	CryptoCurrencyRepository: Hasonlóan működik, mint a StockRepository, de a kriptovalutákkal kapcsolatos adatokat kezeli.
Mindkét repository osztály tartalmaz metódusokat az RSI alapú tanácsadáshoz, melyek visszaadják a legmagasabb és legalacsonyabb RSI-vel rendelkező értékpapírt vagy kriptovalutát.
Logic osztályok
Az alkalmazás üzleti logikája itt kerül megvalósításra:
•	StockLogic: Olyan funkciókat valósít meg, mint az adatok API-ból történő lekérése, vagy az adatok fájlba írása.
•	CryptoCurrencyLogic: Hasonló a StockLogic-hoz, de a kriptovalutákra vonatkozó üzleti logikát tartalmazza.
Exceptions
•	WrongInputException: Egyedi kivétel, amelyet akkor dob az alkalmazás, ha a felhasználó hibás adatot ad meg (például nem numerikus értéket, amikor számot vár a rendszer).
Összefoglalás
Ez az alkalmazás egy jól strukturált, rétegelt architektúrájú Java program, amely konzolos felületen keresztül teszi lehetővé részvények és kriptovaluták adatainak kezelését. Az alkalmazás használatával a felhasználók egyszerűen lekérdezhetik az aktuális piacokat, és döntést hozhatnak befektetéseikről, mindezt egy egyszerű és áttekinthető konzolos felületen keresztül.
A rendszer a felhasználói bevitel és az adatbázis műveletek szempontjából is robusztus, és figyelembe veszi az esetleges hibás bemeneteket. Az RSI alapú tanácsadás lehetővé teszi, hogy a felhasználók gyorsan áttekintést kapjanak arról, melyik részvény vagy kriptovaluta érdemes a vásárlásra vagy az eladásra.

