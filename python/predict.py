import torch
import torch.nn.functional as F
from PIL import Image
from torchvision import transforms
from flask import Flask, jsonify

app = Flask(__name__)


def predict():
    model = torch.load(r'modle')
    model = model.eval().to(torch.device('cpu'))
    model.to('cpu')
    test_transform = transforms.Compose([transforms.Resize(256), transforms.CenterCrop(224), transforms.ToTensor(),
                                         transforms.Normalize(mean=[0.485, 0.456, 0.406], std=[0.229, 0.224, 0.225])])
    img_path = r'end.jpg'
    img_pil = Image.open(img_path)
    input_img = test_transform(img_pil)
    input_img = input_img.unsqueeze(0)
    pred_logits = model(input_img)
    pred_softmax = F.softmax(pred_logits, dim=1)
    n = 3
    top_n = torch.topk(pred_softmax, n)
    pred_ids = top_n[1].cpu().detach().numpy().squeeze()
    confs = top_n[0].cpu().detach().numpy().squeeze()
    if pred_ids[0] == 1:
        word = "青铜器"
    elif pred_ids[0] == 0:
        word = "瓷器"
    else:
        word = "玉器"
    return word


@app.route('/predict', methods=['POST', 'GET'])
def hello_world():
    return predict()


if __name__ == '__main__':
    app.run(host='0.0.0.0', port=8080, debug=True)
