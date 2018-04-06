To start crawling use following command:

scrapy crawl <NAME> -o <OUTPUT>.json -t json

OUTPUT: Filename (& Filepath)
NAME can be: "apo", "doc_ktn", "doc_bgld", "doc_stmk", "doc_noe", "doc_wien"

If you want to debug the scraper, just start with the "runner.py" script and set a break-point in your spider.
Just configure the correct one in the "runner.py" script

ATTENTION:
"doc_wien" is only per gender -> you need to run it two times:
scrapy crawl doc_wien -a sex=M -o docs_wien_male.json -t json
scrapy crawl doc_wien -a sex=W -o docs_wien_female.json -t json