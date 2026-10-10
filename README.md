# 3D drátový renderer – PGRF1 2025, úloha 3

![Ukázka aplikace](docs/images/screenshot.png)

Desktopová aplikace v Javě (Swing), která vykresluje drátové (wireframe) modely 3D těles a kubických křivek.
Celá grafická pipeline je napsaná ručně: od modelovací, pohledové a projekční transformace přes ořezání
v homogenních souřadnicích a dehomogenizaci až po rasterizaci úseček Bresenhamovým algoritmem do `BufferedImage`.
Projekt je postavený podle architektury **MVC** (Model – View – Controller).

Autorka: Lenka Šedivá

## Funkce

- **Tělesa:** krychle, pyramida, koule, torus, válec
- **Kubické křivky:** Bézierova, Coonsova, Fergusonova a parametrická Vivianiho křivka
- **Osy X, Y, Z** jako barevné šipky (červená, zelená, modrá), na které se nepoužívá modelovací transformace
- **Transformace vybraného objektu:** posun, rotace kolem os X/Y/Z, změna měřítka kolem středu objektu
- **Animace:** průběžná rotace vybraného objektu (60 FPS)
- **Kamera:** pohyb pozorovatele, otáčení myší, přepínání mezi 1. a 3. osobou
- **Projekce:** perspektivní a ortogonální
- **Výběr objektu** klávesou nebo kliknutím myší (vybere se objekt, jehož vrchol je nejblíž kurzoru); vybraný objekt se zvýrazní žlutě
- **Změna barvy** objektu přes dialog `JColorChooser`
- **Posuvník přesnosti křivek:** počet bodů, ze kterých se křivky skládají (10–300)

## Ovládání

### Výběr objektu

| Klávesa | Objekt |
|---|---|
| `K` | krychle |
| `M` | pyramida |
| `U` | koule |
| `T` | torus |
| `V` | válec |
| `B` | Bézierova křivka |
| `N` | Coonsova křivka |
| `F` | Fergusonova křivka |
| `I` | Vivianiho křivka |
| levé tlačítko myši | objekt nejblíže kurzoru |

### Práce s vybraným objektem

| Klávesa | Akce |
|---|---|
| šipky | posun |
| `X` / `Y` / `Z` | rotace o 90° kolem osy X / Y / Z |
| `+` / `-` | zvětšení / zmenšení |
| `R` | zapnutí / vypnutí animace rotace |
| `L` | změna barvy |
| `O` | vrácení všech objektů do původního stavu |
| `C` | smazání plátna (osy zůstanou) |

### Kamera a projekce

| Klávesa | Akce |
|---|---|
| `W` `A` `S` `D` | pohyb pozorovatele |
| `Q` / `E` | natočení kamery vlevo / vpravo |
| `Shift` / `Ctrl` | pohyb nahoru / dolů |
| tažení myší | otáčení kamery |
| `Mezerník` | přepnutí mezi 1. a 3. osobou |
| `P` | perspektivní projekce (výchozí) |
| `H` | ortogonální projekce |

Nápověda ke všem klávesám se zobrazuje i přímo v okně aplikace.

## Spuštění

Požadavky: **JDK 22 nebo novější** (kód používá nepojmenované proměnné `_` v lambdách).

### IntelliJ IDEA

Otevřete složku projektu, nastavte SDK na JDK 22+ a spusťte třídu `App`.

### Příkazová řádka

```bash
javac -d out $(find src -name "*.java")
java -cp out App
```

## Struktura projektu

```
src/
├── App.java                      vstupní bod, vytvoří okno a controller
├── controller/
│   ├── Controller.java           rozhraní controlleru
│   └── Controller3D.java         scéna, ovládání klávesnicí a myší, animace
├── model/
│   ├── objectdata/
│   │   ├── Line.java             úsečka ve 2D
│   │   └── mesh/
│   │       ├── Mesh.java         základní třída tělesa (vertex buffer, index buffer, modelovací matice)
│   │       ├── Cube, Pyramid, Sphere, Torus, Cylinder
│   │       ├── Axis, ArrowX/Y/Z  osy souřadného systému
│   │       └── cubics/
│   │           ├── Curve.java    abstraktní křivka, generuje body podle přesnosti
│   │           └── Bezier, Coons, Ferguson, VivianiCurve
│   ├── rasterdata/
│   │   ├── Raster.java           rozhraní rastru
│   │   └── RasterBI.java         raster nad BufferedImage
│   └── rasterops/
│       ├── rasterizer/
│       │   ├── LineRasterizer.java
│       │   └── LineRasterizerBresenham.java
│       └── renderer/
│           └── Renderer3D.java   grafická pipeline
├── transforms/                   knihovna pro vektory, matice, kameru a kubiky (PGRF FIM UHK)
└── view/
    ├── Window.java               hlavní okno (JFrame)
    └── Panel.java                kreslicí plocha, nápověda, posuvník přesnosti
```

## Grafická pipeline

Každé těleso je uložené jako seznam vrcholů (`vertexBuffer`) a seznam dvojic indexů tvořících hrany (`indexBuffer`).
`Renderer3D` pro každou hranu:

1. vynásobí oba vrcholy maticí **MVP** (modelovací × pohledová × projekční),
2. provede **ořezání** v homogenních souřadnicích: `-w ≤ x ≤ w`, `-w ≤ y ≤ w`, `0 ≤ z ≤ w`,
3. provede **dehomogenizaci** (vydělení souřadnicí `w`),
4. provede **transformaci do okna** (převrácení osy Y a převod z intervalu `[-1, 1]` na pixely),
5. vykreslí úsečku **Bresenhamovým algoritmem**.

Pohledovou matici počítá třída `Camera` (azimut, zenit, poloha, režim 1./3. osoby). Projekce je buď
`Mat4PerspRH` (zorný úhel 90°), nebo `Mat4OrthoRH`.

## Použité knihovny

- Java Swing / AWT
- balíček `transforms` od PGRF FIM UHK (matice, vektory, kamera, kubiky)
