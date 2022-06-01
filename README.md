# MySpellBuddy
Android aplikacija za automatsku provjeru diktata korištenjem Googleov ML Kita

## Motivacija
Diktat kao transkripcija izgovorenog teksta čest je način provjere pravopisa u školama 
te se upotrebljava za provjeru pravopisa hrvatskog jezika, ali i stranih jezika koje su učenici upisali. 
Svrha ovog rada je izraditi mobilnu aplikaciju za platformu Android koja će omogućiti samostalno vježbanje 
diktata u različitim jezicima koristeći automatsko prepoznavanje napisanog teksta.

## Korisnčki zahtjevi
Zvučni i tekstualni zapisi tekstova koji će se koristiti za diktate bit će pohranjeni na platformi Firebase. 
Učenik svoje vježbanje započinje skeniranje koda koji jednoznačno određuje tekst koji se diktira, nakon čega se u 
mobilnoj aplikaciji reproducira zvučni zapis teksta. 
Ovisno o postavkama, omogućiti ponavljanje i privremeno zaustavljanje reprodukcije. 
Učenik tekst može pisati na papiru, pa u aplikaciji mora biti omogućeno slikanje napisanog teksta, 
ili ga može pisati pomoću olovke na za to predviđenim mobilnim uređajima (tabletima). 
Prepoznati tekst se mora moći usporediti s diktiranim tekstom te se učeniku trebaju prikazati pogreške. 

## Tehnologije
Za skeniranje koda, prepoznavanje teksta iz slika te prepoznavanje digitalno pisanog teksta koristiti alate iz 
Googleovog razvojnog kompleta ML Kit.
Aplikacija je napisana u programskom jeziku Kotlin, a za spremanje podataka je korišten Firebaseov Storage.
