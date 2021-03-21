#!/bin/bash

wget https://www.gutenberg.org/cache/epub/feeds/catalog.rdf.zip
unzip catalog.rdf.zip
rm catalog.rdf.zip
sed 's/&pg/pg/g' catalog.rdf >catalog_no_pg.rdf
rm catalog.rdf
sed 's/&f/f/g' catalog_no_pg.rdf >catalog_no_f.rdf
rm catalog_no_pg.rdf
sed 's/&lic/lic/g' catalog_no_f.rdf >catalog_no_lic.rdf
rm catalog_no_f.rdf
xmlstarlet ed -d '//comment()' catalog_no_lic.rdf > catalog.rdf
rm catalog_no_lic.rdf