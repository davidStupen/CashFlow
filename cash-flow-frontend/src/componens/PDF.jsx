import api from "../api"

const PDF = (props) => {
  const pdfDom = async () => {
    try {
      const response = await api.get(`/api/cash/items/${props.userId}?pdf=true`, {
        responseType: "blob"
      })
      const urlBlob = new Blob([response.data], {type: "application/pdf"})
      const createUrl = URL.createObjectURL(urlBlob)
      window.open(createUrl)
    }catch(err){
      console.error(err)
    }
}
  return (
    <div>
      <button className="new-btn-style" onClick={pdfDom}>PDF</button>
    </div>
  )
}

export default PDF;