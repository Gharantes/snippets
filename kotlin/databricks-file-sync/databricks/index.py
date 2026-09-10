from bs4 import BeautifulSoup
import zipfile
import pandas as pd
from pandas import DataFrame

def read_soup(
    soup: BeautifulSoup, 
    name: str, 
    index: int,
    df: DataFrame
):


    nfeProc = soup.find("nfeProc")
    procCancNFe = soup.find("procCancNFe")
    procEventoNFe = soup.find("procEventoNFe")

    tipo: str 
    cnpj: str

    if nfeProc is not None:
        tipo = "NFE"
    elif procCancNFe is not None:
        tipo = "CANC"
    elif procEventoNFe is not None:
        tipo = "EVENTO"
    else:
        return
    
    if tipo == "EVENTO":
        infEvento_tag = soup.find("infEvento")
        cnpj_tag = infEvento_tag.find("CNPJ")
        cnpj = cnpj_tag.text if cnpj_tag else None
    elif tipo == "NFE":
        emit = soup.find("emit")
        cnpj_tag = emit.find("CNPJ")
        cnpj = cnpj_tag.text if cnpj_tag else None
        mod_tag = soup.find("mod")
        mod = mod_tag.text if mod_tag else None
        if mod != "55":
            return
        infProt_tag = soup.find("infProt")
        if infProt_tag is None:
            return
    elif tipo == "CANC":
        chNFe_tag = soup.find("chNFe")
        chNFe = chNFe_tag.text if chNFe_tag else None
        cnpj = chNFe[6:20] if chNFe else None
    
    if cnpj is None:
        return
    
    df.loc[index] = [name, cnpj, tipo]

def open_xml(path: str) -> DataFrame:
    df = pd.DataFrame(
        columns=[
            "filename",
            "cnpj",
            "tipo"
        ]
    )
    with open(path, "rb") as f:
        soup = BeautifulSoup(f.read(), "lxml-xml")
        read_soup(soup, f.name, 0, df)
    return df

def iterate_zip(path: str) -> DataFrame:
    index = 0
    df = pd.DataFrame(
        columns=[
            "filename",
            "cnpj",
            "tipo"
        ]
    )
    zip = zipfile.ZipFile(path)
    for arq in zip.namelist():
        if arq.lower().endswith(".xml"):
            with (zip.open(arq, "r") as f):
                soup = BeautifulSoup(f, "lxml-xml")
                read_soup(soup, arq, index, df)
                index = index+1
    return df

def open_file(path: str) -> DataFrame:
    if file.lower().endswith(".xml"):
        return open_xml(file)
    elif file.lower().endswith(".zip"):
        return iterate_zip(file)
    else:
        return pd.DataFrame(
            columns=[
                "filename",
                "cnpj",
                "tipo"
            ]
        )
    
file = "/Volumes/workspace/default/testing/upload/all_w_new.zip"
df = open_file(file)
print(df)

df['cnpj_raiz'] = df['cnpj'].str[:8]
print(df)

counts = df.groupby("cnpj_raiz").size().reset_index(name="count")
print(counts)

counts = df.groupby(["cnpj_raiz", "tipo"]).size().reset_index(name="count")
print(counts)