  handlePdfClick(documentId){
    console.log(documentId);
    fetch("http://localhost:8080/download/document?documentId=documentId",
      method: 'GET'
    ).then(response=> response.blob()).then(res => {
      console.log(res.data.size);
      console.log(res.headers);
      console.log(res.headers["content-disposition"]);

      const url = window.URL.createObjectURL(new Blob([res.data]));
      const link = document.createElement("a");
      link.href = url;
      link.download = res.headers["content-disposition"];
      document.body.appendChild(link);
      link.click();
    });
  }