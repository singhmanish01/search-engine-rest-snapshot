<script src="https://unpkg.com/axios/dist/axios.min.js">
</script>
class UploadForm extends Component {
  state = {
    document_name: "",
    author: "",
    publisher: "",
    language: "",
    number_of_pages: 0,
    doc_file: null,
    isPublic: false,
    redirect: false
  };
  onChange = (e) => {
    const name = e.target.name;
    const value = e.target.type === "file" ? e.target.files[0] : e.target.value;
    this.setState({
      [name]: value,
    });
  };
  handleSubmit = (e) => {
    e.preventDefault();
    console.log(this.state.doc_file);

    const doc_obj = {
      authorName: this.state.author,
      publication: this.state.publisher,
      isPublic: this.state.isPublic,
      language: this.state.language,
      documentName: this.state.document_name,
      documentType: "pdf",
    };

    const data = new FormData();

    data.append("file", this.state.doc_file);

    const doc_obj_string = JSON.stringify(doc_obj);
    data.append("document", doc_obj_string);

    for (const element of data.entries()) {
      console.log(element);
    }

    const contentType = {
      headers: {
        "content-type": "multipart/form-data",
        Authorization: "Bearer " + localStorage.getItem("token"),
      },
      onUploadProgress: (progressEvent) => {
        console.log(
          "upload progress: " +
            Math.round((progressEvent.loaded / progressEvent.total) * 100) +
            "%"
        );
      },
    };
    axios
      .post("/add-document", data, contentType)
      .then((res) => {
        console.log(res);
      })
      .catch((err) => {
        console.log(err);
      });

      this.setState({
        redirect:true
      })
  };
  render() {
    if(this.state.redirect){
      return <Redirect to='/cataloger-dashboard' />
    } else{
    return <Redirect to='/upload-form' />
    }
    }
 }